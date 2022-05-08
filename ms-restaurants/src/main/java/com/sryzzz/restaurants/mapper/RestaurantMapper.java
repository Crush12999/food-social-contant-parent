package com.sryzzz.restaurants.mapper;

import com.sryzzz.commons.model.pojo.Restaurant;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author sryzzz
 * @create 2022/5/8 23:03
 * @description 餐厅 mapper
 */
public interface RestaurantMapper {

    /**
     * 查询餐厅信息
     * @return
     */
    @Select("select id, name, cnName, x, y, location, cnLocation, area, telephone, " +
            "email, website, cuisine, average_price, introduction, thumbnail, like_votes," +
            "dislike_votes, city_id, is_valid, create_date, update_date" +
            " from t_restaurants")
    List<Restaurant> findAll();

}
