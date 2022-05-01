package com.sryzzz.diners.controller;

import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.dto.DinersDTO;
import com.sryzzz.commons.utils.ResultInfoUtil;
import com.sryzzz.diners.service.DinersService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author sryzzz
 * @create 2022/4/30 23:23
 * @description 食客服务控制层
 */
@Api(tags = "食客相关接口")
@RestController
public class DinersController {

    @Resource
    private DinersService dinersService;

    @Resource
    private HttpServletRequest request;

    /**
     * 登录
     *
     * @param account  账号：用户名、手机、邮箱
     * @param password 密码
     * @return 登录结果信息
     */
    @GetMapping("signin")
    public ResultInfo signIn(String account, String password) {
        return dinersService.signIn(account, password, request.getServletPath());
    }

    /**
     * 校验手机号是否已注册
     *
     * @param phone 手机号
     * @return
     */
    @GetMapping("checkPhone")
    public ResultInfo checkPhone(String phone) {
        dinersService.checkPhoneIsRegister(phone);
        return ResultInfoUtil.buildSuccess(request.getServletPath());
    }

    /**
     * 注册
     *
     * @param dinersDTO 食客信息
     * @return 注册结果
     */
    @PostMapping("register")
    public ResultInfo register(@RequestBody DinersDTO dinersDTO) {
        return dinersService.register(dinersDTO, request.getServletPath());
    }
}
