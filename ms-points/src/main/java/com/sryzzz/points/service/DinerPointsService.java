package com.sryzzz.points.service;

import com.sryzzz.commons.constant.RedisKeyConstant;
import com.sryzzz.commons.model.pojo.DinerPoints;
import com.sryzzz.commons.utils.AssertUtil;
import com.sryzzz.points.mapper.DinerPointsMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author sryzzz
 * @create 2022/5/5 22:30
 * @description 积分业务逻辑层
 */
@Service
public class DinerPointsService {

    @Resource
    private DinerPointsMapper dinerPointsMapper;

    /**
     * 添加积分
     *
     * @param dinerId 食客ID
     * @param points  积分
     * @param types   类型 0=签到，1=关注好友，2=添加Feed，3=添加商户评论
     */
    @Transactional(rollbackFor = Exception.class)
    public void addPoints(Integer dinerId, Integer points, Integer types) {
        // 基本参数校验
        AssertUtil.isTrue(dinerId == null || dinerId < 1, "食客不能为空");
        AssertUtil.isTrue(points == null || points < 1, "积分不能为空");
        AssertUtil.isTrue(types == null, "请选择对应的积分类型");

        // 插入数据库
        DinerPoints dinerPoints = new DinerPoints();
        dinerPoints.setFkDinerId(dinerId);
        dinerPoints.setPoints(points);
        dinerPoints.setTypes(types);
        dinerPointsMapper.save(dinerPoints);

    }
}
