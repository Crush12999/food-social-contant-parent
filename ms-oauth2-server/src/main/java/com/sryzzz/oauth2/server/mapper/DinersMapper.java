package com.sryzzz.oauth2.server.mapper;

import com.sryzzz.commons.model.pojo.Diners;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author sryzzz
 * @create 2022/4/30 21:54
 * @description 食客 Mapper
 */
public interface DinersMapper {

    /**
     * 根据用户名 or 手机号 or 邮箱查询用户信息
     * @param account
     * @return
     */
    @Select("select id, username, nickname, phone, email, " +
            "password, avatar_url, roles, is_valid from t_diners where " +
            "(username = #{account} or phone = #{account} or email = #{account})")
    Diners selectByAccountInfo(@Param("account") String account);

}
