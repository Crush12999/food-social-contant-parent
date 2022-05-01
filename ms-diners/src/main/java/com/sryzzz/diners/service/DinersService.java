package com.sryzzz.diners.service;

import cn.hutool.core.bean.BeanUtil;
import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.utils.AssertUtil;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.diners.config.OAuth2ClientConfiguration;
import com.sryzzz.diners.domain.OAuthDinerInfo;
import com.sryzzz.diners.vo.LoginDinerInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @author sryzzz
 * @create 2022/4/30 23:03
 * @description 食客业务逻辑层
 */
@Service
public class DinersService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service.name.ms-oauth-server}")
    private String oauthServerName;

    @Resource
    private OAuth2ClientConfiguration oAuth2ClientConfiguration;

    /**
     * 登录
     *
     * @param account  账号：用户名、手机、邮箱
     * @param password 密码
     * @param path     请求路径
     * @return
     */
    public ResultInfo signIn(String account, String password, String path) {

        // 参数校验
        AssertUtil.isNotEmpty(account, "请输入登录账号！");
        AssertUtil.isNotEmpty(password, "请输入登录密码！");

        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 构建请求体（请求参数）
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("username", account);
        body.add("password", password);
        body.setAll(BeanUtil.beanToMap(oAuth2ClientConfiguration));
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        // 设置 Authorization
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(oAuth2ClientConfiguration.getClientId(),
                oAuth2ClientConfiguration.getSecret()));

        // 发送请求
        ResponseEntity<ResultInfo> result = restTemplate.postForEntity(oauthServerName + "oauth/token", entity, ResultInfo.class);

        // 处理返回结果
        AssertUtil.isTrue(result.getStatusCode() != HttpStatus.OK, "登录失败！");
        ResultInfo resultInfo = result.getBody();
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            // 登录失败
            resultInfo.setData(resultInfo.getMessage());
            return resultInfo;
        }
        
        // 这里的 data 是一个LinkedHashMap 转成了域对象 OAuthDinerInfo
        OAuthDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new OAuthDinerInfo(), false);

        // 根据业务需求返回视图对象
        LoginDinerInfo loginDinerInfo = new LoginDinerInfo();
        loginDinerInfo.setToken(dinerInfo.getAccessToken());
        loginDinerInfo.setAvatarUrl(dinerInfo.getAvatarUrl());
        loginDinerInfo.setNickname(dinerInfo.getNickname());

        return ResultInfoUtil.buildSuccess(path, loginDinerInfo);
    }
}
