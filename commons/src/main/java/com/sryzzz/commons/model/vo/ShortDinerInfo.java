package com.sryzzz.commons.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author sryzzz
 * @create 2022/5/3 11:49
 * @description 关注食客信息
 */
@Getter
@Setter
@ApiModel(description = "关注食客信息")
public class ShortDinerInfo implements Serializable {

    @ApiModelProperty("主键")
    public Integer id;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("头像")
    private String avatarUrl;
}
