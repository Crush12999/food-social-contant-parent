package com.sryzzz.diners.controller;

import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.diners.service.SignService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author sryzzz
 * @create 2022/5/5 13:58
 * @description 签到的控制层
 */
@RestController
@RequestMapping("sign")
public class SignController {

    @Resource
    private SignService signService;

    @Resource
    private HttpServletRequest request;

    /**
     * 获取当月签到情况
     *
     * @param access_token 登录用户token
     * @param date     日期字符串（yyyy-MM-dd）
     * @return 当月签到情况
     */
    @GetMapping
    public ResultInfo getSignInfo(String access_token, String date) {
        Map<String, Boolean> map = signService.getSignInfo(access_token, date);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), map);
    }

    /**
     * 获取签到次数
     *
     * @param access_token 登录用户的token
     * @param date         传入日期
     * @return 某月签到次数
     */
    @GetMapping("count")
    public ResultInfo getSignCount(String access_token, String date) {
        Long count = signService.getSignCount(access_token, date);
        return ResultInfoUtil.buildSuccess(request.getServletPath(), count);
    }

    /**
     * 用户签到，可以补签
     *
     * @param access_token 登录用户的token
     * @param date         传入日期相当于补签
     * @return
     */
    @PostMapping
    public ResultInfo<Integer> sign(String access_token,
                                    @RequestParam(required = false) String date) {
        int count = signService.doSign(access_token, date);
        return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE, "签到成功", request.getServletPath(), count);
    }
}
