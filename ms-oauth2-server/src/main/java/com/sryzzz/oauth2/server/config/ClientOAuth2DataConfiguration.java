package com.sryzzz.oauth2.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sryzzz
 * @create 2022/4/30 18:24
 * @description 客户端配置类
 */
@Component
@ConfigurationProperties(prefix = "client.oauth2")
@Data
public class ClientOAuth2DataConfiguration {

    private String clientId;

    /**
     * 客户端安全码
     */
    private String secret;

    /**
     * 授权类型
     */
    private String[] grantTypes;

    /**
     * token有效期
     */
    private int tokenValidityTime;

    /**
     * refresh-token有效期
     */
    private int refreshTokenValidityTime;

    /**
     * 客户端访问范围
     */
    private String[] scopes;
}
