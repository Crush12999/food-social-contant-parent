package com.sryzzz.feeds.controller;

import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.pojo.Feeds;
import com.sryzzz.commons.model.vo.FeedsVO;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.feeds.service.FeedsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sryzzz
 * @create 2022/5/3 16:29
 * @description Feeds 控制层
 */
@RestController
public class FeedsController {

    @Resource
    private FeedsService feedsService;

    @Resource
    private HttpServletRequest request;


    /**
     * 分页获取关注的 Feed 数据
     *
     * @param page
     * @param access_token
     * @return
     */
    @GetMapping("{page}")
    public ResultInfo selectForPage(@PathVariable Integer page, String access_token) {
        List<FeedsVO> feedsVOS = feedsService.selectForPage(page, access_token);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), feedsVOS);
    }


    /**
     * 删除 Feed
     *
     * @param id
     * @param access_token
     * @return
     */
    @DeleteMapping("{id}")
    public ResultInfo deleteFeed(@PathVariable Integer id, String access_token) {
        feedsService.delete(id, access_token);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "删除成功");
    }

    /**
     * 添加 Feed
     *
     * @param feeds
     * @param access_token
     * @return
     */
    @PostMapping
    public ResultInfo create(@RequestBody Feeds feeds, String access_token) {
        feedsService.create(feeds, access_token);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "添加成功");
    }


    /**
     * 变更 Feed
     *
     * @param followingDinerId
     * @param access_token
     * @param type
     * @return
     */
    @PostMapping("updateFollowingFeeds/{followingDinerId}")
    public ResultInfo addFollowingFeed(@PathVariable Integer followingDinerId,
                                       String access_token, @RequestParam int type) {
        feedsService.addFollowingFeed(followingDinerId, access_token, type);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "操作成功");
    }
}
