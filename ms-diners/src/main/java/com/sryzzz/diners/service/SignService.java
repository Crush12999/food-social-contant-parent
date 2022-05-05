package com.sryzzz.diners.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.sryzzz.commons.constant.ApiConstant;
import com.sryzzz.commons.constant.PointTypesConstant;
import com.sryzzz.commons.exception.ParameterException;
import com.sryzzz.commons.model.domain.ResultInfo;
import com.sryzzz.commons.model.vo.SignInDinerInfo;
import com.sryzzz.commons.utils.AssertUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author sryzzz
 * @create 2022/5/5 13:29
 * @description 签到业务逻辑层
 */
@Service
public class SignService {

    @Value("${service.name.ms-oauth-server}")
    private String oauthServerName;

    @Value("${service.name.ms-points-server}")
    private String pointsServerName;

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 获取当月签到情况
     *
     * @param accessToken 登录用户token
     * @param dataStr     日期字符串（yyyy-MM-dd）
     * @return
     */
    public Map<String, Boolean> getSignInfo(String accessToken, String dataStr) {
        // 获取某月签到情况，默认当月
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 获取登录用户信息
        Date date = getDate(dataStr);
        // 构建 key
        String signKey = buildSignKey(dinerInfo.getId(), date);
        // 构建一个自动排序的 Map
        Map<String, Boolean> signInfo = new TreeMap<>();
        // 获取某月的天数，考虑闰年
        int dayOfMonth = DateUtil.lengthOfMonth(DateUtil.month(date) + 1,
                DateUtil.isLeapYear(DateUtil.dayOfYear(date)));

        // bitfield user:sign:5:202205 u30 0
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);
        List<Long> list = redisTemplate.opsForValue().bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return signInfo;
        }
        long v = list.get(0) == null ? 0 : list.get(0);
        // 从低位到高位进行遍历，为 0 表示未签到， 为 1 表示已签到
        for (int i = dayOfMonth; i > 0; i--) {
            // 签到：yyyy-MM-01 true
            // 未签到： yyyy-MM-02 false
            LocalDateTime localDateTime = LocalDateTimeUtil.of(date).withDayOfMonth(i);
            boolean flag = v >> 1 << 1 != v;
            signInfo.put(DateUtil.format(localDateTime, "yyyy-MM-dd"), flag);
            v >>= 1;
        }
        return signInfo;
    }

    /**
     * 获取用户签到次数
     *
     * @param accessToken 登录用户token
     * @param dataStr     日期字符串（2022-05-26）
     * @return 用户签到次数（默认本月）
     */
    public long getSignCount(String accessToken, String dataStr) {
        // 获取登录用户信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 获取日期
        Date date = getDate(dataStr);
        // 构建key
        String signKey = buildSignKey(dinerInfo.getId(), date);
        // e.g. BITCOUNT user:sign:dinerId:202205
        return (long) redisTemplate.execute(
                (RedisCallback<Long>) con -> con.bitCount(signKey.getBytes())
        );
    }

    /**
     * 用户签到
     *
     * @param accessToken 登录用户token
     * @param dateStr     日期字符串（2022-05-26）
     * @return
     */
    public int doSign(String accessToken, String dateStr) {
        // 获取登录用户信息
        SignInDinerInfo dinerInfo = loadSignInDinerInfo(accessToken);
        // 获取日期，校验传入日期是否在当前日期之前，不允许提前签到
        Date date = getDate(dateStr);
        checkSignParamDate(date);
        // 获取日期对应的天数，这个月的多少号
        int offset = DateUtil.dayOfMonth(date) - 1;
        // 构建 key
        String signKey = buildSignKey(dinerInfo.getId(), date);
        // 查看是否已签到
        boolean isSigned = redisTemplate.opsForValue().getBit(signKey, offset);
        AssertUtil.isTrue(isSigned, "当前日期已签到，无需重复签到");
        // 签到
        redisTemplate.opsForValue().setBit(signKey, offset, true);
        // 统计连续签到的次数
        int count = getContinuousSignCount(dinerInfo.getId(), date);
        // 赠送签到积分并返回
        int points = addPoints(count, dinerInfo.getId());
        return points;
    }

    /**
     * 对传入的补签时间进行限制
     *
     * @param date 传入的补签时间
     */
    private void checkSignParamDate(Date date) {
        Date now = new Date();
        String paramMonth = DateUtil.format(date, "MM");
        String nowMonth = DateUtil.format(now, "MM");
        AssertUtil.isTrue(!paramMonth.equals(nowMonth), "只能对当前月份补签");
        String paramDate = DateUtil.format(date, "yyyyMMdd");
        String nowDate = DateUtil.format(now, "yyyyMMdd");
        AssertUtil.isTrue(paramDate.compareTo(nowDate) > 0, "无法提前签到");
    }

    /**
     * 统计连续签到的次数
     *
     * @param dinerId 用户id
     * @param date    时间
     * @return 连续签到的次数
     */
    private int getContinuousSignCount(Integer dinerId, Date date) {
        // 获取日期对应的天数，多少号，假设是31
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 构建 key
        String signKey = buildSignKey(dinerId, date);

        // bitfield user:sgin:5:202205 u31 0
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                .valueAt(0);
        List<Long> list = redisTemplate.opsForValue().bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int signCount = 0;
        long v = list.get(0) == null ? 0 : list.get(0);
        // i 表示唯一操作次数
        for (int i = dayOfMonth; i > 0; i--) {
            // 右移再左移，如果等于自己说明最低位是 0，表示未签到
            if (v >> 1 << 1 == v) {
                // 低位 0 且非当天说明连续签到中断了
                if (i != dayOfMonth) {
                    break;
                }
            } else {
                signCount++;
            }
            v >>= 1;
        }
        return signCount;
    }

    /**
     * 构造 Redis Key -- user:sign:dinerId:yyyyMM
     *
     * @param dinerId 用户id
     * @param date    时间
     * @return 签到的Redis的key
     */
    private String buildSignKey(Integer dinerId, Date date) {
        return String.format("user:sign:%d:%s", dinerId, DateUtil.format(date, "yyyyMM"));
    }

    /**
     * 获取日期
     *
     * @param dateStr 日期字符串（2022-05-26）
     * @return 日期对象
     */
    private Date getDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return new Date();
        }
        try {
            return DateUtil.parseDate(dateStr);
        } catch (Exception e) {
            throw new ParameterException("请传入yyyy-MM-dd的日期格式");
        }
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
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        SignInDinerInfo dinerInfo = BeanUtil.fillBeanWithMap((LinkedHashMap) resultInfo.getData(),
                new SignInDinerInfo(), false);
        if (dinerInfo == null) {
            throw new ParameterException(ApiConstant.NO_LOGIN_CODE, ApiConstant.NO_LOGIN_MESSAGE);
        }
        return dinerInfo;
    }

    /**
     * 添加用户积分
     *
     * @param count         连续签到次数
     * @param signInDinerId 登录用户id
     * @return 获取的积分
     */
    private int addPoints(int count, Integer signInDinerId) {
        // 签到1天送10积分，连续签到2天送20积分，3天送30积分，4天以上均送50积分
        int points = 10;
        if (count == 2) {
            points = 20;
        } else if (count == 3) {
            points = 30;
        } else if (count >= 4) {
            points = 50;
        }
        // 调用积分接口添加积分
        // 构建请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 构建请求体（请求参数）
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("dinerId", signInDinerId);
        body.add("points", points);
        body.add("types", PointTypesConstant.sign.getType());
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, headers);
        // 发送请求
        ResponseEntity<ResultInfo> result = restTemplate.postForEntity(pointsServerName,
                entity, ResultInfo.class);
        AssertUtil.isTrue(result.getStatusCode() != HttpStatus.OK, "登录失败！");
        ResultInfo resultInfo = result.getBody();
        if (resultInfo.getCode() != ApiConstant.SUCCESS_CODE) {
            // 失败了, 事务要进行回滚
            throw new ParameterException(resultInfo.getCode(), resultInfo.getMessage());
        }
        return points;
    }
}
