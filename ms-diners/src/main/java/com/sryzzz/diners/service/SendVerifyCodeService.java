package com.sryzzz.diners.service;

import cn.hutool.core.util.RandomUtil;
import com.sryzzz.commons.constant.RedisKeyConstant;
import com.sryzzz.commons.utils.AssertUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author sryzzz
 * @create 2022/5/1 13:26
 * @description 发送验证码的业务逻辑层
 */
@Service
public class SendVerifyCodeService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 发送验证码
     *
     * @param phone 手机号
     */
    public void send(String phone) {
        // 检查非空
        AssertUtil.isNotEmpty(phone, "手机号不能为空");

        // 根据手机号查询是否已生成验证码，已生成直接返回
        if (!checkCodeIsExpired(phone)) {
            return;
        }

        // 生成 6 位验证码
        String code = RandomUtil.randomNumbers(6);

        // 调用短信服务发送短信

        // 发送成功，将 code 保存至 Redis，设置失效时间 60s
        String key = RedisKeyConstant.verify_code.getKey() + phone;
        redisTemplate.opsForValue().set(key, code, 60, TimeUnit.SECONDS);
    }

    /**
     * 根据手机号查询是否已生成验证码
     *
     * @param phone 手机号
     * @return 已生成：false，未生成：true
     */
    private boolean checkCodeIsExpired(String phone) {
        String key = RedisKeyConstant.verify_code.getKey() + phone;
        String code = redisTemplate.opsForValue().get(key);
        return StringUtils.isBlank(code);
    }

    /**
     * 根据手机号获取验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    public String getCodeByPhone(String phone) {
        String key = RedisKeyConstant.verify_code.getKey() + phone;
        String code = redisTemplate.opsForValue().get(key);
        return code;
    }
}
