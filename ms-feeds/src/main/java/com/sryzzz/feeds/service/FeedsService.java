package com.sryzzz.feeds.service;

import cn.hutool.core.bean.BeanUtil;
import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.constant.RedisKeyConstant;
import com.sryzzz.commons.exception.ParameterException;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.pojo.Feeds;
import com.sryzzz.commons.model.vo.SignInDinerInfo;
import com.sryzzz.commons.utils.AssertUtil;
import com.sryzzz.feeds.mapper.FeedsMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author sryzzz
 * @create 2022/5/3 16:28
 * @description Feeds 业务处理层
 */
@Service
public class FeedsService {

    @Resource
    private FeedsMapper feedsMapper;

    @Value("${service.name.ms-oauth-server}")
    private String oauthServerName;

    @Value("${service.name.ms-follow-server}")
    private String followServerName;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 添加 feed
     *
     * @param feeds       feed信息
     * @param accessToken 用户token
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(Feeds feeds, String accessToken) {
        // 非空校验 Feed 内容不能为空，不能太长
        AssertUtil.isNotEmpty(feeds.getContent(), "请输入内容");
        AssertUtil.isTrue(feeds.getContent().length() > 255, "输入内容过多，请重新输入");

        // 获取登录用户信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);

        // Feed 去关联用户信息
        feeds.setFkDinerId(dinerInfo.getId());

        // 添加 Feed
        int count = feedsMapper.save(feeds);
        AssertUtil.isTrue(count == 0, "添加失败");

        // 推送到粉丝的列表中 -- 后续这里应该采用异步消息队列来解决性能问题
        // 先获取粉丝 id 集合
        List<Integer> followers = findFollowers(dinerInfo.getId());
        // 推送 Feed
        long now = System.currentTimeMillis();
        followers.forEach(follower -> {
            String key = RedisKeyConstant.following_feeds.getKey() + follower;
            redisTemplate.opsForZSet().add(key, feeds.getId(), now);
        });
    }

    /**
     * 获取粉丝列表（id集合）
     *
     * @param dinerId 用户 id
     * @return 粉丝 id 集合
     */
    private List<Integer> findFollowers(Integer dinerId) {
        String url = followServerName + "followers/" + dinerId;
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        List<Integer> followers = (List<Integer>) resultInfo.getData();
        return followers;
    }


    /**
     * 获取登录用户信息
     *
     * @param accessToken 登录用户令牌
     * @return 登录用户信息
     */
    private SignInDinerInfo loadSignInDinerInfo(String accessToken) {
        // 必须登录
        AssertUtil.mustLogin(accessToken);
        String url = oauthServerName + "user/me?access_token={accessToken}";
        ResultInfo resultInfo = restTemplate.getForObject(url, ResultInfo.class, accessToken);
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            throw new ParameterException(resultInfo.getMessage());
        }
        SignInDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new SignInDinerInfo(), false);
        return dinerInfo;
    }
}
