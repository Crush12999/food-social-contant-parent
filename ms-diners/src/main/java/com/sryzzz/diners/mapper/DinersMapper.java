package com.sryzzz.diners.mapper;

import com.sryzzz.commons.model.pojo.Diners;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author sryzzz
 * @create 2022/5/1 13:43
 * @description 食客 DAO 层
 */
public interface DinersMapper {

    /**
     * 根据手机号查询食客信息
     *
     * @param phone 手机号
     * @return 用户信息
     */
    @Select("select id, username, phone, email, is_valid " +
            " from t_diners where phone = #{phone}")
    Diners selectByPhone(@Param("phone") String phone);

    /**
     * 根据用户名查询食客信息
     *
     * @param username 用户名
     * @return
     */
    @Select("select id, username, phone, email, is_valid " +
            " from t_diners where username = #{username}")
    Diners selectByUsername(@Param("username") String username);
}
