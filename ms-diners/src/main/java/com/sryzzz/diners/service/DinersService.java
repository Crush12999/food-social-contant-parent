package com.sryzzz.diners.service;

import cn.hutool.core.bean.BeanUtil;
import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.dto.DinersDTO;
import com.sryzzz.commons.model.pojo.Diners;
import com.sryzzz.commons.model.vo.ShortDinerInfo;
import com.sryzzz.commons.utils.AssertUtil;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.diners.config.OAuth2ClientConfiguration;
import com.sryzzz.diners.domain.OAuthDinerInfo;
import com.sryzzz.diners.mapper.DinersMapper;
import com.sryzzz.diners.vo.LoginDinerInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

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

    @Resource
    private DinersMapper dinersMapper;

    @Resource
    private SendVerifyCodeService sendVerifyCodeService;

    /**
     * 校验手机号是否已注册
     *
     * @param phone 手机号
     */
    public void checkPhoneIsRegister(String phone) {
        AssertUtil.isNotEmpty(phone, "手机号不能为空");
        Diners diners = dinersMapper.selectByPhone(phone);
        AssertUtil.isTrue(diners == null, "该手机号未注册");
        AssertUtil.isTrue(diners.getIsValid() == 0, "该用户已锁定，请先联系管理员进行解锁");

    }

    /**
     * 登录
     *
     * @param account  账号：用户名、手机、邮箱
     * @param password 密码
     * @param path     请求路径
     * @return 结果
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

    /**
     * 用户注册
     *
     * @param dinersDTO 注册用户信息
     * @param path      路径
     * @return 结果
     */
    public ResultInfo register(DinersDTO dinersDTO, String path) {
        // 参数非空校验
        String username = dinersDTO.getUsername();
        AssertUtil.isNotEmpty(username, "请输入用户名");
        String password = dinersDTO.getPassword();
        AssertUtil.isNotEmpty(password, "请输入密码");
        String phone = dinersDTO.getPhone();
        AssertUtil.isNotEmpty(phone, "请输入手机号");
        String verifyCode = dinersDTO.getVerifyCode();
        AssertUtil.isNotEmpty(verifyCode, "请输入验证码");

        // 获取验证码
        String code = sendVerifyCodeService.getCodeByPhone(phone);
        // 验证码是否过期
        AssertUtil.isNotEmpty(code, "验证码已过期，请重新发送");
        // 验证码一致性校验
        AssertUtil.isTrue(!dinersDTO.getVerifyCode().equals(code), "输入的验证码不一致");

        // 验证用户名是否已注册
        Diners diners = dinersMapper.selectByUsername(username);
        AssertUtil.isTrue(diners != null, "用户名已存在，请重新输入");

        // 注册
        // 密码加密
        dinersDTO.setPassword(DigestUtils.md5Hex(password.trim()));
        dinersMapper.save(dinersDTO);

        // 自动登录
        return signIn(username.trim(), password.trim(), path);
    }

    /**
     * 根据 ids 查询食客信息
     *
     * @param ids 主键 id，多个以逗号分隔，逗号之间不用空格
     * @return
     */
    public List<ShortDinerInfo> findByIds(String ids) {
        AssertUtil.isNotEmpty(ids);
        String[] idArr = ids.split(",");
        List<ShortDinerInfo> dinerInfos = dinersMapper.findByIds(idArr);
        return dinerInfos;
    }
}
