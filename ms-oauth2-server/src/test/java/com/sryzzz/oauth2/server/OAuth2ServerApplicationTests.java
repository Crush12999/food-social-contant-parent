package com.sryzzz.oauth2.server;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/5/1 19:15
 * @description
 */
@SpringBootTest
@AutoConfigureMockMvc
public class OAuth2ServerApplicationTests {

    @Resource
    protected MockMvc mockMvc;
}
