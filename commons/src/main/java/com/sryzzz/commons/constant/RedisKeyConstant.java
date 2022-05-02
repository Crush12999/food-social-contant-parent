package com.sryzzz.commons.constant;

import lombok.Getter;

/**
 * @author sryzzz
 * @create 2022/5/1 13:20
 * @description Redis Key 公共枚举类
 */
@Getter
public enum RedisKeyConstant {

    /**
     * 验证码
     */
    verify_code("verify_code:", "验证码"),

    /**
     * 秒杀券的key
     */
    seckill_vouchers("seckill_vouchers:", "秒杀券的key"),

    /**
     * 分布式锁的key
     */
    lock_key("lockby:", "分布式锁的key"),

    /**
     * 关注集合key
     */
    following("following:", "关注集合key"),

    /**
     * 粉丝集合key
     */
    followers("followers:", "粉丝集合key")

    ;

    private String key;
    private String desc;

    RedisKeyConstant(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

}
