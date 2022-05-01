package com.sryzzz.oauth2.server.service;

import com.sryzzz.commons.model.domain.SignInIdentity;
import com.sryzzz.commons.model.pojo.Diners;
import com.sryzzz.commons.utils.AssertUtil;
import com.sryzzz.oauth2.server.mapper.DinersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/4/30 21:53
 * @description 用户自定义登录逻辑，校验
 */
@Service
public class UserService implements UserDetailsService {

    @Resource
    private DinersMapper dinersMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AssertUtil.isNotEmpty(username, "请输入用户名！");
        Diners diners = dinersMapper.selectByAccountInfo(username);
        if (diners == null) {
            throw new UsernameNotFoundException("用户名或密码错误，请重新输入！");
        }
        // 初始化登录认证对象
        SignInIdentity signInIdentity = new SignInIdentity();
        BeanUtils.copyProperties(diners, signInIdentity);
        return signInIdentity;
    }
}
