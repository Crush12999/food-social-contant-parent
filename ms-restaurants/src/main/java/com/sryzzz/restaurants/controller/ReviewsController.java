package com.sryzzz.restaurants.controller;

import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.restaurants.service.ReviewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sryzzz
 * @create 2022/5/9 00:39
 * @description
 */
@RestController
@RequestMapping("reviews")
public class ReviewsController {

    @Resource
    private ReviewsService reviewsService;
    @Resource
    private HttpServletRequest request;

    /**
     * 添加餐厅评论
     *
     * @param restaurantId
     * @param access_token
     * @param content
     * @param likeIt
     * @return
     */
    @PostMapping("{restaurantId}")
    public ResultInfo<String> addReview(@PathVariable Integer restaurantId,
                                        String access_token,
                                        @RequestParam("content") String content,
                                        @RequestParam("likeIt") int likeIt) {
        reviewsService.addReview(restaurantId, access_token, content, likeIt);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "添加成功");
    }

}
