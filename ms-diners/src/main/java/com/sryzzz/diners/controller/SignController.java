package com.sryzzz.diners.controller;

import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.diners.service.SignService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
     * 用户签到，可以补签
     *
     * @param access_token 登录用户的token
     * @param date 传入日期相当于补签
     * @return
     */
    @PostMapping
    public ResultInfo<Integer> sign(String access_token,
                                    @RequestParam(required = false) String date) {
        int count = signService.doSign(access_token, date);
        return ResultInfoUtil.build(ApiConstant.SUCCESS_CODE, "签到成功", request.getServletPath(), count);
    }
}
