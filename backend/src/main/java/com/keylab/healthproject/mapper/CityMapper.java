package com.keylab.healthproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CityMapper {

    //获取城市名称和人口数量
    @Select("SELECT h.name AS city_name, SUM(CAST(c.total AS UNSIGNED)) AS total_count " +
            "FROM hospital h " +
            "JOIN community c ON h.dept_id = c.dep_id " +
            "GROUP BY h.name")
    List<Map<String, Object>> getCityNameAndNum();

    //根据城市id获取人口数量
    @Select("SELECT SUM(CAST(total AS UNSIGNED)) AS total_sum " +
            "FROM community " +
            "WHERE dep_id = #{id}")
    long getNumByCityId(Integer i);

    //获取男女人口数量
    @Select("SELECT " +
            "SUM(CASE WHEN gender = '男' THEN 1 ELSE 0 END) AS 男, " +
            "SUM(CASE WHEN gender = '女' THEN 1 ELSE 0 END) AS 女 " +
            "FROM person_data " +
            "WHERE gender IN ('男', '女')")
    List<Map<String, Object>> getSexCount();

    //根据城市id获取男女人口数量
    @Select("SELECT " +
            "SUM(CASE WHEN male IS NOT NULL THEN CAST(male AS UNSIGNED) ELSE 0 END) AS 男, " +
            "SUM(CASE WHEN female IS NOT NULL THEN CAST(female AS UNSIGNED) ELSE 0 END) AS 女 " +
            "FROM community " +
            "WHERE dep_id = #{depId} " +
            "GROUP BY dep_id")
    List<Map<String, Object>> getSexCountByCity(Integer i);

    //返回各个省所有社区健康情况累加和
    @Select("SELECT " +
            "h.name AS hospital_name, " +
            "SUM(c.total) AS total, " +
            "SUM(c.female) AS female, " +
            "SUM(c.male) AS male, " +
            "SUM(c.family_num) AS family_num, " +
            "SUM(c.age_30) AS age_30, " +
            "SUM(c.age_30_40) AS age_30_40, " +
            "SUM(c.age_40_50) AS age_40_50, " +
            "SUM(c.age_50_60) AS age_50_60, " +
            "SUM(c.age_60_70) AS age_60_70, " +
            "SUM(c.age_70) AS age_70, " +
            "SUM(c.normal) AS normal, " +
            "SUM(c.warn_level1) AS warn_level1, " +
            "SUM(c.warn_level2) AS warn_level2, " +
            "SUM(c.warn_level3) AS warn_level3 " +
            "FROM community c " +
            "JOIN hospital h ON c.dep_id = h.dept_id " +
            "GROUP BY h.name")
    List<Map<String, Object>> getHealthStatus();

    @Select("SELECT " +
            "name, " +
            "total, " +
            "female, " +
            "male, " +
            "family_num, " +
            "age_30, " +
            "age_30_40, " +
            "age_40_50, " +
            "age_50_60, " +
            "age_60_70, " +
            "age_70, " +
            "normal, " +
            "warn_level1, " +
            "warn_level2, " +
            "warn_level3 " +
            "FROM community " +
            "WHERE dep_id = #{depId}")
    List<Map<String, Object>> getHealthStatusByCity(Integer depId);
}
