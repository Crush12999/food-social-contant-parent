package com.sryzzz.feeds.controller;

import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.pojo.Feeds;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.feeds.service.FeedsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
     * 添加 Feed
     *
     * @param feeds
     * @param access_token
     * @return
     */
    @PostMapping
    public ResultInfo<String> create(@RequestBody Feeds feeds, String access_token) {
        feedsService.create(feeds, access_token);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "添加成功");
    }
}
