package com.sryzzz.oauth2.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author sryzzz
 * @create 2022/5/1 13:24
 * @description Redis配置类
 */
// @Configuration
public class RedisTemplateConfiguration {

    // @Bean
    /*public RedisConnectionFactory lettuceConnectionFactory() {
        RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
                .master("mymaster")
                .sentinel("192.168.131.61", 26379)
                .sentinel("192.168.131.64", 26379)
                .sentinel("192.168.131.65", 26379);
        sentinelConfiguration.setDatabase(1);
        sentinelConfiguration.setPassword("123456");
        return new LettuceConnectionFactory(sentinelConfiguration);
    }*/

    public RedisConnectionFactory lettuceConnectionFactory() {
        RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration()
                .clusterNode("192.168.131.61", 6371)
                .clusterNode("192.168.131.61", 6372)
                .clusterNode("192.168.131.64", 6373)
                .clusterNode("192.168.131.64", 6374)
                .clusterNode("192.168.131.65", 6375)
                .clusterNode("192.168.131.65", 6376);
        clusterConfiguration.setMaxRedirects(5);
        clusterConfiguration.setPassword("123456");
        return new LettuceConnectionFactory(clusterConfiguration);
    }
}
