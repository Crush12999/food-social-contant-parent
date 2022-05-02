package com.sryzzz.follow.mapper;

import com.sryzzz.commons.model.pojo.Follow;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author sryzzz
 * @create 2022/5/1 23:45
 * @description 关注/取关 Mapper
 */
public interface FollowMapper {

    /**
     * 查询关注信息
     * @param dinerId 食客ID
     * @param followDinerId 被关注者ID
     * @return 关注信息
     */
    @Select("select id, diner_id, follow_diner_id, is_valid from t_follow " +
            "where diner_id = #{dinerId} and follow_diner_id = #{followDinerId}")
    Follow selectFollow(@Param("dinerId") Integer dinerId, @Param("followDinerId") Integer followDinerId);

    /**
     * 添加关注信息
     * @param dinerId 食客ID
     * @param followDinerId 被关注者ID
     * @return 关注信息
     */
    @Insert("insert into t_follow (diner_id, follow_diner_id, is_valid, create_date, update_date)" +
            " values(#{dinerId}, #{followDinerId}, 1, now(), now())")
    int save(@Param("dinerId") Integer dinerId, @Param("followDinerId") Integer followDinerId);

    /**
     * 修改关注信息
     * @param id
     * @param isFollowed 是否关注
     * @return
     */
    @Update("update t_follow set is_valid = #{isFollowed}, update_date = now() where id = #{id}")
    int update(@Param("id") Integer id, @Param("isFollowed") int isFollowed);

}