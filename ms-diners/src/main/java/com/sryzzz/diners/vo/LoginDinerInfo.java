package com.sryzzz.diners.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


/**
 * @author sryzzz
 * @create 2022/4/30 23:03
 * @description 视图对象
 */
@Setter
@Getter
public class LoginDinerInfo implements Serializable {

    private String nickname;
    private String token;
    private String avatarUrl;

}