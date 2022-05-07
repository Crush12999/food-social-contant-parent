package com.sryzzz.diners.service;

import cn.hutool.core.bean.BeanUtil;
import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.constant.RedisKeyConstant;
import com.sryzzz.commons.exception.ParameterException;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.vo.SignInDinerInfo;
import com.sryzzz.commons.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @author sryzzz
 * @create 2022/5/6 23:10
 * @description 附近的人业务层
 */
@Service
public class NearMeService {

    @Value("${service.name.ms-oauth-server}")
    private String oauthServerName;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 更新食客坐标
     *
     * @param accessToken 登录用户 token
     * @param lon         经度
     * @param lat         纬度
     */
    public void updateDinerLocation(String accessToken, Float lon, Float lat) {
        // 参数校验
        AssertUtil.isTrue(lon == null, "获取经度失败");
        AssertUtil.isTrue(lat == null, "获取纬度失败");
        // 获取登录用户信息
        SignInDinerInfo signInDinerInfo = loadSignInDinerInfo(accessToken);
        // 获取 key diner:location
        String key = RedisKeyConstant.diner_location.getKey();
        // 将用户地理位置信息存入 Redis
        RedisGeoCommands.GeoLocation geoLocation = new RedisGeoCommands
                .GeoLocation(signInDinerInfo.getId(), new Point(lon, lat));
        redisTemplate.opsForGeo().add(key, geoLocation);
    }

    /**
     * 获取登录用户信息
     *
     * @param accessToken
     * @return
     */
    private SignInDinerInfo loadSignInDinerInfo(String accessToken) {
        // 必须登录
        AssertUtil.mustLogin(accessToken);
        String url = oauthServerName + "user/me?access_token={accessToken}";
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class, accessToken);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        SignInDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new SignInDinerInfo(), false);
        if (dinerInfo == null) {
            throw new ParameterException(ApiConstant.NO_LOGIN_CODE, ApiConstant.NO_LOGIN_MESSAGE);
        }
        return dinerInfo;
    }
}
