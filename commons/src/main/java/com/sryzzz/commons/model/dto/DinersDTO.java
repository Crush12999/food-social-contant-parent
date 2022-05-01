package com.sryzzz.commons.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author sryzzz
 * @create 2022/5/1 15:20
 * @description 注册用户信息
 */
@Getter
@Setter
@ApiModel(description = "注册用户信息")
@ToString
public class DinersDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("验证码")
    private String verifyCode;

}
