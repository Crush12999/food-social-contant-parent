package com.sryzzz.points;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author sryzzz
 * @create 2022/5/5 22:29
 * @description
 */
@MapperScan("com.sryzzz.points.mapper")
@SpringBootApplication
public class PointsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PointsApplication.class, args);
    }
}
