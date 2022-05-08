package com.sryzzz.restaurants.service;

import cn.hutool.core.bean.BeanUtil;
import com.sryzzz.commons.constant.RedisKeyConstant;
import com.sryzzz.commons.model.pojo.Restaurant;
import com.sryzzz.commons.utils.AssertUtil;
import com.sryzzz.restaurants.mapper.RestaurantMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedHashMap;

/**
 * @author sryzzz
 * @create 2022/5/8 23:39
 * @description 餐厅业务逻辑层
 */
@Service
@Slf4j
public class RestaurantService {

    @Resource
    public RestaurantMapper restaurantMapper;
    @Resource
    public RedisTemplate redisTemplate;

    /**
     * 根据餐厅 ID 查询餐厅数据
     *
     * @param restaurantId
     * @return
     */
    public Restaurant findById(Integer restaurantId) {
        // 请选择餐厅
        AssertUtil.isTrue(restaurantId == null, "请选择餐厅查看");
        // 获取 Key
        String key = RedisKeyConstant.restaurants.getKey() + restaurantId;
        // 获取餐厅缓存
        LinkedHashMap restaurantMap = (LinkedHashMap) redisTemplate.opsForHash().entries(key);
        // 如果缓存不存在，查询数据库
        Restaurant restaurant = null;
        if (restaurantMap == null || restaurantMap.isEmpty()) {
            log.info("缓存失效了，查询数据库：{}", restaurantId);
            // 查询数据库
            restaurant = restaurantMapper.findById(restaurantId);
            if (restaurant != null) {
                // 更新缓存
                redisTemplate.opsForHash().putAll(key, BeanUtil.beanToMap(restaurant));
            } else {
                // 写入缓存一个空数据，设置一个失效时间，60s
                // redisTemplate.execute()
            }
        } else {
            restaurant = BeanUtil.fillBeanWithMap(restaurantMap,
                    new Restaurant(), false);
        }
        return restaurant;
    }
}
