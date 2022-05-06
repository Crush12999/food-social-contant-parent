package com.sryzzz.points.mapper;

import com.sryzzz.commons.model.pojo.DinerPoints;
import com.sryzzz.commons.model.vo.DinerPointsRankVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    /**
     * 查询积分排行榜 topN
     *
     * @param top 前 top 个
     * @return 积分排行前top的信息
     */
    @Select("SELECT p.id, p.total, p.nickname, p.avatar_url, p.ranks " +
            "FROM ( SELECT A.*, " +
                        "IF (@pre_sum != A.total, @rank := @rank + @tmp, @rank) AS ranks, " +
                        "IF (@pre_sum = A.total, @tmp := @tmp + 1, @tmp := 1), " +
                        "@pre_sum := A.total " +
                    "FROM ( SELECT t1.fk_diner_id id, sum(t1.points) total, t2.nickname, t2.avatar_url " +
                            "FROM t_diner_points t1 " +
                            "LEFT JOIN t_diners t2 ON t1.fk_diner_id = t2.id " +
                            "WHERE t1.is_valid = 1 AND t2.is_valid = 1 " +
                            "GROUP BY t1.fk_diner_id " +
                            "ORDER BY sum(t1.points) DESC LIMIT #{top}) A, " +
                            "( SELECT @rank := 1, @pre_sum := NULL, @tmp := 1) r ) p;")
    List<DinerPointsRankVO> findTopN(@Param("top") int top);

    /**
     * 根据食客 ID 查询当前食客的积分排名
     *
     * @param dinerId 食客 ID
     * @return 积分排名信息
     */
    @Select("SELECT p.id, p.total, p.nickname, p.avatar_url, p.ranks " +
            "FROM ( SELECT A.*, " +
                        "IF (@pre_sum != A.total, @rank := @rank + @tmp, @rank) AS ranks, " +
                        "IF (@pre_sum = A.total, @tmp := @tmp + 1, @tmp := 1)," +
                        "@pre_sum := A.total " +
                    "FROM ( SELECT t1.fk_diner_id id, sum(t1.points) total, t2.nickname, t2.avatar_url " +
                            "FROM t_diner_points t1 " +
                            "LEFT JOIN t_diners t2 ON t1.fk_diner_id = t2.id " +
                            "WHERE t1.is_valid = 1 AND t2.is_valid = 1 " +
                            "GROUP BY t1.fk_diner_id ORDER BY sum(t1.points) DESC ) A, " +
                            "( SELECT @rank := 1, @pre_sum := NULL, @tmp := 1) r ) p WHERE id = #{dinerId};")
    DinerPointsRankVO findDinerRank(@Param("dinerId") int dinerId);
}
