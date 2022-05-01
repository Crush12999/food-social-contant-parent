package com.sryzzz.diners;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sryzzz
 * @create 2022/4/28 00:56
 * @description 食客服务（用户）启动类
 */
@MapperScan("com.sryzzz.diners.mapper")
@SpringBootApplication
public class DinersApplication {

    public static void main(String[] args) {
        SpringApplication.run(DinersApplication.class);
    }
}
