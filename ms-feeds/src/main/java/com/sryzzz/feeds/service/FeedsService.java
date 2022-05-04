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
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
     * 变更 Feed
     *
     * @param followingDinerId 关注好友的ID
     * @param accessToken      登录用户的Token
     * @param type             1关注  0取关
     */
    @Transactional(rollbackFor = Exception.class)
    public void addFollowingFeed(Integer followingDinerId, String accessToken, int type) {
        // 请选择关注的好友
        AssertUtil.isTrue(followingDinerId == null || followingDinerId < 1, "请选择关注的好友");
        // 获取登录信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 获取关注/取关的食客的所有 Feed
        List<Feeds> feedsList = feedsMapper.findByDinerId(followingDinerId);

        String key = RedisKeyConstant.following_feeds.getKey() + dinerInfo.getId();
        if (type == 0) {
            // 取关
            List<Integer> feedIds = feedsList.stream()
                    .map(feeds -> feeds.getId()).collect(Collectors.toList());
            redisTemplate.opsForZSet().remove(key, feedIds.toArray(new Integer[]{}));
        } else {
            // 关注
            Set<ZSetOperations.TypedTuple> typedTuples = feedsList.stream()
                    .map(feed -> new DefaultTypedTuple<>(feed.getId(), (double) feed.getUpdateDate().getTime()))
                    .collect(Collectors.toSet());
            redisTemplate.opsForZSet().add(key, typedTuples);
        }

    }

    /**
     * 删除feed
     *
     * @param id
     * @param accessToken
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id, String accessToken) {
        // 请选择要删除的 Feed
        AssertUtil.isTrue(id == null || id < 1, "请选择要删除的Feed");
        // 获取登录用户
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 获取 Feed 内容
        Feeds feeds = feedsMapper.findById(id);
        // 判断 Feed 是否已被删除且只能删除自己的 Feed
        AssertUtil.isTrue(feeds == null, "该Feed已被删除");
        AssertUtil.isTrue(!feeds.getFkDinerId().equals(dinerInfo.getId()), "只能删除自己的Feed");
        // 删除
        int count = feedsMapper.delete(id);
        if (count == 0) {
            return;
        }
        // 将内容从粉丝的集合中删除 -- 异步消息队列优化
        // 先获取我的粉丝
        List<Integer> followers = findFollowers(dinerInfo.getId());
        // 移除 Feed
        followers.forEach(follower -> {
            String key = RedisKeyConstant.following_feeds.getKey() + follower;
            redisTemplate.opsForZSet().remove(key, feeds.getId());
        });
    }

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
