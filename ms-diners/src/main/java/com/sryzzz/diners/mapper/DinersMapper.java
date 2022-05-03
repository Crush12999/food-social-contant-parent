package com.sryzzz.diners.mapper;

import com.sryzzz.commons.model.dto.DinersDTO;
import com.sryzzz.commons.model.pojo.Diners;
import com.sryzzz.commons.model.vo.ShortDinerInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 新增食客信息
     *
     * @param dinersDTO 食客信息
     * @return
     */
    @Insert("insert into " +
            " t_diners (username, password, phone, roles, is_valid, create_date, update_date) " +
            " values (#{username}, #{password}, #{phone}, 'ROLE_USER', 1, now(), now())")
    int save(DinersDTO dinersDTO);

    /**
     * 根据 ID 集合查询多个食客信息
     *
     * @param ids 食客id列表
     * @return 多个食客信息
     */
    @Select("<script> " +
            " select id, nickname, avatar_url from t_diners " +
            " where is_valid = 1 and id in " +
            " <foreach item=\"id\" collection=\"ids\" open=\"(\" separator=\",\" close=\")\"> " +
            "   #{id} " +
            " </foreach> " +
            " </script>")
    List<ShortDinerInfo> findByIds(@Param("ids") String[] ids);
}
