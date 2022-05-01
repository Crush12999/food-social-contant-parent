package com.sryzzz.registry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author sryzzz
 * @create 2022/4/28 00:28
 * @description 激活注册中心组件
 */
@EnableEurekaServer
@SpringBootApplication
public class RegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class);
    }
}
