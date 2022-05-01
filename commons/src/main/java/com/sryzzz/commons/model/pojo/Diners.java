package com.sryzzz.commons.model.pojo;

import com.sryzzz.commons.model.base.BaseModel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sryzzz
 * @create 2022/4/30 21:59
 * @description 食客实体类
 */
@Getter
@Setter
public class Diners extends BaseModel {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 角色
     */
    private String roles;
}
