package com.sryzzz.commons.model.pojo;

import com.sryzzz.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sryzzz
 * @create 2022/5/2 22:10
 * @description 食客关注实体类
 */
@ApiModel(description = "食客关注实体类")
@Getter
@Setter
@ToString
public class Follow extends BaseModel {

    @ApiModelProperty("用户ID")
    private int dinerId;

    @ApiModelProperty("关注用户ID")
    private Integer followDinerId;

}
