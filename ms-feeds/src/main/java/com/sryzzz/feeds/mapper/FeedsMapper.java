package com.sryzzz.feeds.mapper;

import com.sryzzz.commons.model.pojo.Feeds;
import org.apache.ibatis.annotations.*;

/**
 * @author sryzzz
 * @create 2022/5/1 13:43
 * @description Feed Mapper 层
 */
public interface FeedsMapper {

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