package com.sryzzz.diners.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sryzzz
 * @create 2022/4/28 00:58
 * @description
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public String hello(String name) {
        return "hello " + name;
    }
}
