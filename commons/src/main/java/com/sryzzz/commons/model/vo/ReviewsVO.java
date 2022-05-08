package com.sryzzz.commons.model.vo;

import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sryzzz.commons.model.pojo.Reviews;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/5/9 00:25
 * @description 餐厅评论实体类
 */
@ToString
@Getter
@Setter
@ApiModel(description = "餐厅评论实体类")
public class ReviewsVO extends Reviews {

    @ApiModelProperty("食客信息")
    private ShortDinerInfo dinerInfo;

    @ApiModelProperty(value = "创建日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date createDate;

}
