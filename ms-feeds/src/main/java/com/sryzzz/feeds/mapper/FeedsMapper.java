package com.sryzzz.feeds.mapper;

import com.sryzzz.commons.model.pojo.Feeds;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author sryzzz
 * @create 2022/5/1 13:43
 * @description Feed Mapper 层
 */
public interface FeedsMapper {


    /**
     * 根据食客 ID 查询 Feed
     *
     * @param dinerId 食客 ID
     * @return Feed集合
     */
    @Select("select id, content, update_date from t_feeds " +
            " where fk_diner_id = #{dinerId} and is_valid = 1")
    List<Feeds> findByDinerId(@Param("dinerId") Integer dinerId);

    /**
     * 查询 Feed
     *
     * @param id
     * @return
     */
    @Select("select id, content, fk_diner_id, praise_amount, " +
            " comment_amount, fk_restaurant_id, create_date, update_date, is_valid " +
            " from t_feeds where id = #{id} and is_valid = 1")
    Feeds findById(@Param("id") Integer id);

    /**
     * 逻辑删除 Feed
     *
     * @param id
     * @return
     */
    @Update("update t_feeds set is_valid = 0 where id = #{id} and is_valid = 1")
    int delete(@Param("id") Integer id);


    /**
     * 添加 Feed
     *
     * @param feeds feeds信息
     * @return 插入影响行数
     */
    @Insert("insert into t_feeds (content, fk_diner_id, praise_amount, " +
            " comment_amount, fk_restaurant_id, create_date, update_date, is_valid) " +
            " values (#{content}, #{fkDinerId}, #{praiseAmount}, #{commentAmount}, #{fkRestaurantId}, " +
            " now(), now(), 1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int save(Feeds feeds);

}