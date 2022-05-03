package com.sryzzz.commons.model.pojo;

import com.sryzzz.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sryzzz
 * @create 2022/5/3 16:13
 * @description Feed信息类
 */
@Getter
@Setter
@ToString
@ApiModel(description = "Feed信息类")
public class Feeds extends BaseModel {

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("食客")
    private Integer fkDinerId;

    @ApiModelProperty("点赞")
    private int praiseAmount;

    @ApiModelProperty("评论")
    private int commentAmount;

    @ApiModelProperty("关联的餐厅")
    private Integer fkRestaurantId;

}
