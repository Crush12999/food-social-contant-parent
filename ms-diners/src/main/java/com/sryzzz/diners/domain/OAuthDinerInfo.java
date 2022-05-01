package com.sryzzz.diners.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author sryzzz
 * @create 2022/4/30 23:03
 * @description 域对象
 */
@Getter
@Setter
public class OAuthDinerInfo implements Serializable {

    private String nickname;
    private String avatarUrl;
    private String accessToken;
    private String expireIn;
    private List<String> scopes;
    private String refreshToken;

}