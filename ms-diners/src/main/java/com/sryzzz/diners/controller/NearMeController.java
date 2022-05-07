package com.sryzzz.diners.controller;

import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.diners.service.NearMeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sryzzz
 * @create 2022/5/8 00:07
 * @description 附近的人控制层
 */
@RequestMapping("nearme")
@RestController
public class NearMeController {

    @Resource
    private NearMeService nearMeService;

    @Resource
    private HttpServletRequest request;

    /**
     * 更新食客坐标
     *
     * @param access_token 登录用户token
     * @param lon          经度
     * @param lat          纬度
     * @return
     */
    @PostMapping
    public ResultInfo updateDinerLocation(String access_token,
                                          @RequestParam Float lon,
                                          @RequestParam Float lat) {
        nearMeService.updateDinerLocation(access_token, lon, lat);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), "更新成功");
    }
}
