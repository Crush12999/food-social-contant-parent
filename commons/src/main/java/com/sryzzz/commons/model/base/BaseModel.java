package com.sryzzz.commons.model.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author sryzzz
 * @create 2022/4/30 17:33
 * @description 实体对象公共属性
 */
@Getter
@Setter
public class BaseModel implements Serializable {

    private Integer id;
    private Date createDate;
    private Date updateDate;
    private int isValid;

}