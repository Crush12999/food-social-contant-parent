package com.sryzzz.feeds;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sryzzz
 * @create 2022/5/3 16:27
 * @description Feed流启动类
 */
@MapperScan("com.sryzzz.feeds.mapper")
@SpringBootApplication
public class FeedsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedsApplication.class);
    }
}
