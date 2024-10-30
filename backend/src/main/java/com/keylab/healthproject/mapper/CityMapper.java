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
}
