package com.sryzzz.commons.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sryzzz
 * @create 2022/5/1 11:39
 * @description 用户登录信息对象
 */
@Getter
@Setter
@ApiModel(value = "SignInDinerInfo", description = "登录用户信息")
public class SignInDinerInfo implements Serializable {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("角色")
    private String roles;
}
