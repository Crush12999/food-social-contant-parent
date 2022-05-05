package com.sryzzz.commons.model.pojo;

import com.sryzzz.commons.model.base.BaseModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sryzzz
 * @create 2022/5/5 22:15
 * @description 积分实体对象
 */
@Getter
@Setter
@ToString
public class DinerPoints extends BaseModel {

    @ApiModelProperty("关联DinerId")
    private Integer fkDinerId;

    @ApiModelProperty("积分")
    private Integer points;

    @ApiModelProperty(name = "类型", example = "0=签到，1=关注好友，2=添加Feed，3=添加商户评论")
    private Integer types;
}
