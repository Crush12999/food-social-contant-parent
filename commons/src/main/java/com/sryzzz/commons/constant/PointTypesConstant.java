package com.sryzzz.commons.constant;

import lombok.Getter;

/**
 * @author sryzzz
 * @create 2022/5/5 22:51
 * @description 积分类型
 */
@Getter
public enum PointTypesConstant {

    /**
     * 0=签到
     */
    sign(0),

    /**
     * 1=关注好友
     */
    follow(1),

    /**
     * 2=添加Feed
     */
    feed(2),

    /**
     * 3=添加商户评论
     */
    review(3)
    ;

    private int type;

    PointTypesConstant(int key) {
        this.type = key;
    }
}
