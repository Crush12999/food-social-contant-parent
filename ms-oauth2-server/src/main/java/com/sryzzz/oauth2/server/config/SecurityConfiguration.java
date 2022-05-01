package com.sryzzz.oauth2.server.config;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/4/30 18:04
 * @description Security 配置类
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 注入 Redis 连接工厂
     */
    @Resource
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 初始化 RedisTokenStore 用于将 token 存储至 Redis
     */
    @Bean
    public RedisTokenStore redisTokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        // 设置 key 的层次前缀，方便查询
        redisTokenStore.setPrefix("TOKEN:");
        return redisTokenStore;
    }

    /**
     * 初始化密码编码器，用 MD5 加密密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            /**
             * 加密
             * @param rawPassword 原始密码
             * @return 密文
             */
            @Override
            public String encode(CharSequence rawPassword) {
                return DigestUtils.md5Hex(rawPassword.toString());
            }

            /**
             * 校验密码
             * @param rawPassword       原始密码
             * @param encodedPassword   加密密码
             * @return
             */
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return DigestUtils.md5Hex(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }

    /**
     * 初始化认证管理对象
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 放行和认证规则
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                // 放行的请求
                .antMatchers("/oauth/**", "/actuator/**").permitAll()
                .and()
                .authorizeRequests()
                // 其他请求都需要认证
                .anyRequest().authenticated();
    }
}
