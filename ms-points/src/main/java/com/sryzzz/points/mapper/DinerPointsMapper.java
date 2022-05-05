package com.sryzzz.points.mapper;

import com.sryzzz.commons.model.pojo.DinerPoints;
import org.apache.ibatis.annotations.Insert;

/**
 * @author sryzzz
 * @create 2022/5/5 22:18
 * @description 积分Mapper
 */
public interface DinerPointsMapper {

    /**
     * 添加积分
     *
     * @param dinerPoints
     */
    @Insert("insert into t_diner_points (fk_diner_id, points, types, is_valid, " +
            "create_date, update_date) " +
            "values (#{fkDinerId}, #{points}, #{types}, 1, now(), now())")
    void save(DinerPoints dinerPoints);
}
