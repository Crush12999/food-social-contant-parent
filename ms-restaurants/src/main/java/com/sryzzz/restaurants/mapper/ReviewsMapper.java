package com.sryzzz.restaurants.mapper;

import com.sryzzz.commons.model.pojo.Reviews;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

/**
 * @author sryzzz
 * @create 2022/5/9 00:30
 * @description
 */
public interface ReviewsMapper {

    /**
     * 插入餐厅评论
     *
     * @param reviews 餐厅评论
     * @return
     */
    @Insert("insert into t_reviews (fk_restaurant_id, fk_diner_id, content, like_it, is_valid, create_date, update_date)" +
            " values (#{fkRestaurantId}, #{fkDinerId}, #{content}, #{likeIt}, 1, now(), now())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveReviews(Reviews reviews);
}
