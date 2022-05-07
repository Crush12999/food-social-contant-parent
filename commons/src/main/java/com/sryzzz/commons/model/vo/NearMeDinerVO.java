package com.sryzzz.commons.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author sryzzz
 * @create 2022/5/8 00:41
 * @description 附近的人
 */
@ApiModel(description = "附近的人")
@Getter
@Setter
@ToString
public class NearMeDinerVO extends ShortDinerInfo {

    @ApiModelProperty(value = "距离", example = "98m")
    private String distance;
}
