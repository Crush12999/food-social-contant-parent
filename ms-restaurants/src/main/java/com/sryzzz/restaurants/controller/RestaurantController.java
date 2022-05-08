package com.sryzzz.restaurants.controller;

import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.pojo.Restaurant;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.restaurants.service.RestaurantService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sryzzz
 * @create 2022/5/8 23:03
 * @description 餐厅 controller
 */
@RestController
public class RestaurantController {

    @Resource
    private RestaurantService restaurantService;
    @Resource
    private HttpServletRequest request;

    /**
     * 根据餐厅 ID 查询餐厅数据
     *
     * @param restaurantId
     * @return
     */
    @GetMapping("{restaurantId}")
    public ResultInfo<Restaurant> findById(@PathVariable Integer restaurantId) {
        Restaurant restaurant = restaurantService.findById(restaurantId);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), restaurant);
    }

}