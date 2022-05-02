package com.sryzzz.seckill.config;

import com.sryzzz.seckill.model.RedisLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/5/1 23:45
 * @description 初始化 Bean
 */
@Configuration
public class RedisLockConfiguration {

    @Resource
    private RedisTemplate redisTemplate;

    @Bean
    public RedisLock redisLock() {
        RedisLock redisLock = new RedisLock(redisTemplate);
        return redisLock;
    }
}
