package com.keylab.healthproject.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
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

    @Select("""
            WITH MonthlyAverages AS (
                SELECT
                    DATE_FORMAT(create_time, '%Y-%m') AS month,
                    researched_person_id,
                    AVG(breath_rate) AS avg_breath_rate,
                    AVG(systolic) AS avg_systolic,
                    AVG(diastolic) AS avg_diastolic,
                    AVG(blood_oxygen) AS avg_blood_oxygen,
                    AVG(temperature) AS avg_temperature,
                    AVG(heart_rate) AS avg_heart_rate,
                    AVG(blood_glucose) AS avg_blood_glucose
                FROM health_data
                WHERE create_time >= DATE_SUB(CURRENT_DATE, INTERVAL 6 MONTH)  -- 选择最近六个月的数据
                  AND DATE_FORMAT(create_time, '%Y-%m') < DATE_FORMAT(CURRENT_DATE, '%Y-%m')  -- 排除当前月份
                GROUP BY month, researched_person_id
            ),
            HealthLevels AS (
                SELECT
                    month,
                    SUM(CASE WHEN avg_breath_rate BETWEEN 16 AND 20 THEN 1 ELSE 0 END) AS L0_breath_rate,
                    SUM(CASE WHEN avg_breath_rate BETWEEN 11 AND 15 THEN 1 ELSE 0 END) AS L1_breath_rate,
                    SUM(CASE WHEN avg_breath_rate BETWEEN 8 AND 10 THEN 1 ELSE 0 END) AS L2_breath_rate,
                    SUM(CASE WHEN avg_breath_rate < 8 THEN 1 ELSE 0 END) AS L3_breath_rate,
                    SUM(CASE WHEN avg_systolic BETWEEN 90 AND 139 THEN 1 ELSE 0 END) AS L0_systolic,
                    SUM(CASE WHEN avg_systolic BETWEEN 140 AND 159 THEN 1 ELSE 0 END) AS L1_systolic,
                    SUM(CASE WHEN avg_systolic BETWEEN 160 AND 179 OR avg_systolic BETWEEN 80 AND 89 THEN 1 ELSE 0 END) AS L2_systolic,
                    SUM(CASE WHEN avg_systolic >= 180 OR avg_systolic < 80 THEN 1 ELSE 0 END) AS L3_systolic,
                    SUM(CASE WHEN avg_diastolic BETWEEN 60 AND 89 THEN 1 ELSE 0 END) AS L0_diastolic,
                    SUM(CASE WHEN avg_diastolic BETWEEN 90 AND 99 THEN 1 ELSE 0 END) AS L1_diastolic,
                    SUM(CASE WHEN avg_diastolic BETWEEN 100 AND 109 THEN 1 ELSE 0 END) AS L2_diastolic,
                    SUM(CASE WHEN avg_diastolic >= 110 THEN 1 ELSE 0 END) AS L3_diastolic,
                    SUM(CASE WHEN avg_blood_oxygen BETWEEN 95 AND 100 THEN 1 ELSE 0 END) AS L0_blood_oxygen,
                    SUM(CASE WHEN avg_blood_oxygen BETWEEN 90 AND 94 THEN 1 ELSE 0 END) AS L1_blood_oxygen,
                    SUM(CASE WHEN avg_blood_oxygen BETWEEN 85 AND 89 THEN 1 ELSE 0 END) AS L2_blood_oxygen,
                    SUM(CASE WHEN avg_blood_oxygen <= 84 THEN 1 ELSE 0 END) AS L3_blood_oxygen,
                    SUM(CASE WHEN avg_temperature BETWEEN 36 AND 37.3 THEN 1 ELSE 0 END) AS L0_temperature,
                    SUM(CASE WHEN avg_temperature BETWEEN 37.4 AND 39 THEN 1 ELSE 0 END) AS L1_temperature,
                    SUM(CASE WHEN avg_temperature >= 39.1 THEN 1 ELSE 0 END) AS L2_temperature,
                    SUM(CASE WHEN avg_heart_rate BETWEEN 50 AND 120 THEN 1 ELSE 0 END) AS L0_heart_rate,
                    SUM(CASE WHEN avg_heart_rate > 120 AND avg_heart_rate <= 159 THEN 1 ELSE 0 END) AS L1_heart_rate,
                    SUM(CASE WHEN avg_heart_rate >= 160 AND avg_heart_rate < 180 THEN 1 ELSE 0 END) AS L2_heart_rate,
                    SUM(CASE WHEN avg_heart_rate >= 180 OR avg_heart_rate <= 35 THEN 1 ELSE 0 END) AS L3_heart_rate,
                    SUM(CASE WHEN avg_blood_glucose BETWEEN 4 AND 11 THEN 1 ELSE 0 END) AS L0_blood_glucose,
                    SUM(CASE WHEN avg_blood_glucose BETWEEN 11.1 AND 19.9 THEN 1 ELSE 0 END) AS L1_blood_glucose,
                    SUM(CASE WHEN avg_blood_glucose BETWEEN 20 AND 29.9 THEN 1 ELSE 0 END) AS L2_blood_glucose,
                    SUM(CASE WHEN avg_blood_glucose >= 30 OR avg_blood_glucose < 3 THEN 1 ELSE 0 END) AS L3_blood_glucose
                FROM MonthlyAverages
                GROUP BY month
            )
            SELECT
                month,
                SUM(L0_breath_rate) + SUM(L0_systolic) + SUM(L0_diastolic) +
                SUM(L0_blood_oxygen) + SUM(L0_temperature) + SUM(L0_heart_rate) +
                SUM(L0_blood_glucose) AS L0_count,
                SUM(L1_breath_rate) + SUM(L1_systolic) + SUM(L1_diastolic) +
                SUM(L1_blood_oxygen) + SUM(L1_temperature) + SUM(L1_heart_rate) +
                SUM(L1_blood_glucose) AS L1_count,
                SUM(L2_breath_rate) + SUM(L2_systolic) + SUM(L2_diastolic) +
                SUM(L2_blood_oxygen) + SUM(L2_temperature) + SUM(L2_heart_rate) +
                SUM(L2_blood_glucose) AS L2_count,
                SUM(L3_breath_rate) + SUM(L3_systolic) + SUM(L3_diastolic) +
                SUM(L3_blood_oxygen) + SUM(L3_heart_rate) +
                SUM(L3_blood_glucose) AS L3_count
            FROM HealthLevels
            GROUP BY month
            ORDER BY month;
            """)
    List<Map<String, Object>> getHealthLevel();

    @Select("""
            WITH MonthlyAverages AS (
                SELECT
                    DATE_FORMAT(hd.create_time, '%Y-%m') AS month,
                    hd.researched_person_id,
                    AVG(hd.breath_rate) AS avg_breath_rate,
                    AVG(hd.systolic) AS avg_systolic,
                    AVG(hd.diastolic) AS avg_diastolic,
                    AVG(hd.blood_oxygen) AS avg_blood_oxygen,
                    AVG(hd.temperature) AS avg_temperature,
                    AVG(hd.heart_rate) AS avg_heart_rate,
                    AVG(hd.blood_glucose) AS avg_blood_glucose
                FROM health_data hd
                JOIN person_data pd ON hd.researched_person_id = pd.id
                JOIN community c ON pd.dept_id = c.id
                JOIN hospital h ON c.dep_id = h.dept_id
                WHERE h.dept_id = #{Id}
                AND hd.create_time < LAST_DAY(CURDATE()) 
                AND hd.create_time >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) 
                GROUP BY month, hd.researched_person_id
            ),
            HealthLevels AS (
                SELECT
                    month,
                    SUM(CASE WHEN avg_breath_rate BETWEEN 16 AND 20 THEN 1 ELSE 0 END) AS L0_breath_rate,
                    SUM(CASE WHEN avg_breath_rate BETWEEN 11 AND 15 THEN 1 ELSE 0 END) AS L1_breath_rate,
                    SUM(CASE WHEN avg_breath_rate BETWEEN 8 AND 10 THEN 1 ELSE 0 END) AS L2_breath_rate,
                    SUM(CASE WHEN avg_breath_rate < 8 THEN 1 ELSE 0 END) AS L3_breath_rate,
            
                    SUM(CASE WHEN avg_systolic BETWEEN 90 AND 139 THEN 1 ELSE 0 END) AS L0_systolic,
                    SUM(CASE WHEN avg_systolic BETWEEN 140 AND 159 THEN 1 ELSE 0 END) AS L1_systolic,
                    SUM(CASE WHEN avg_systolic BETWEEN 160 AND 179 THEN 1 ELSE 0 END) AS L2_systolic,
                    SUM(CASE WHEN avg_systolic >= 180 OR avg_systolic < 80 THEN 1 ELSE 0 END) AS L3_systolic,
            
                    SUM(CASE WHEN avg_diastolic BETWEEN 60 AND 89 THEN 1 ELSE 0 END) AS L0_diastolic,
                    SUM(CASE WHEN avg_diastolic BETWEEN 90 AND 99 THEN 1 ELSE 0 END) AS L1_diastolic,
                    SUM(CASE WHEN avg_diastolic BETWEEN 100 AND 109 THEN 1 ELSE 0 END) AS L2_diastolic,
                    SUM(CASE WHEN avg_diastolic >= 110 THEN 1 ELSE 0 END) AS L3_diastolic,
            
                    SUM(CASE WHEN avg_blood_oxygen BETWEEN 95 AND 100 THEN 1 ELSE 0 END) AS L0_blood_oxygen,
                    SUM(CASE WHEN avg_blood_oxygen BETWEEN 90 AND 94 THEN 1 ELSE 0 END) AS L1_blood_oxygen,
                    SUM(CASE WHEN avg_blood_oxygen BETWEEN 85 AND 89 THEN 1 ELSE 0 END) AS L2_blood_oxygen,
                    SUM(CASE WHEN avg_blood_oxygen < 85 THEN 1 ELSE 0 END) AS L3_blood_oxygen,
            
                    SUM(CASE WHEN avg_temperature BETWEEN 36 AND 37.3 THEN 1 ELSE 0 END) AS L0_temperature,
                    SUM(CASE WHEN avg_temperature BETWEEN 37.4 AND 39 THEN 1 ELSE 0 END) AS L1_temperature,
                    SUM(CASE WHEN avg_temperature >= 39.1 THEN 1 ELSE 0 END) AS L2_temperature,
            
                    SUM(CASE WHEN avg_heart_rate BETWEEN 50 AND 120 THEN 1 ELSE 0 END) AS L0_heart_rate,
                    SUM(CASE WHEN avg_heart_rate BETWEEN 120 AND 159 THEN 1 ELSE 0 END) AS L1_heart_rate,
                    SUM(CASE WHEN avg_heart_rate >= 160 THEN 1 ELSE 0 END) AS L2_heart_rate,
                    SUM(CASE WHEN avg_heart_rate < 50 OR avg_heart_rate > 180 THEN 1 ELSE 0 END) AS L3_heart_rate,
            
                    SUM(CASE WHEN avg_blood_glucose BETWEEN 4 AND 11 THEN 1 ELSE 0 END) AS L0_blood_glucose,
                    SUM(CASE WHEN avg_blood_glucose BETWEEN 11.1 AND 19.9 THEN 1 ELSE 0 END) AS L1_blood_glucose,
                    SUM(CASE WHEN avg_blood_glucose BETWEEN 20 AND 29.9 THEN 1 ELSE 0 END) AS L2_blood_glucose,
                    SUM(CASE WHEN avg_blood_glucose >= 30 OR avg_blood_glucose < 3 THEN 1 ELSE 0 END) AS L3_blood_glucose
                FROM MonthlyAverages
                GROUP BY month
            )
            SELECT
                month,
                SUM(L0_breath_rate) + SUM(L0_systolic) + SUM(L0_diastolic) +
                SUM(L0_blood_oxygen) + SUM(L0_temperature) + SUM(L0_heart_rate) +
                SUM(L0_blood_glucose) AS L0_count,
                SUM(L1_breath_rate) + SUM(L1_systolic) + SUM(L1_diastolic) +
                SUM(L1_blood_oxygen) + SUM(L1_temperature) + SUM(L1_heart_rate) +
                SUM(L1_blood_glucose) AS L1_count,
                SUM(L2_breath_rate) + SUM(L2_systolic) + SUM(L2_diastolic) +
                SUM(L2_blood_oxygen) + SUM(L2_temperature) + SUM(L2_heart_rate) +
                SUM(L2_blood_glucose) AS L2_count,
                SUM(L3_breath_rate) + SUM(L3_systolic) + SUM(L3_diastolic) +
                SUM(L3_blood_oxygen) + SUM(L3_heart_rate) +
                SUM(L3_blood_glucose) AS L3_count
            FROM HealthLevels
            GROUP BY month
            ORDER BY month;
            """)
    List<Map<String, Object>> getHealthLevelByCity(Integer Id);

    @Select("""
            SELECT
                  total,
                  female,
                  male,
                  family_num,
                  age_30,
                  age_30_40,
                  age_40_50,
                  age_50_60,
                  age_60_70,
                  age_70,
                  normal,
                  warn_level1,
                  warn_level2,
                  warn_level3
                FROM
                  community
                WHERE
                  name = #{communityName}
            
            """)
    Map<String, Object> getHealthDataByCommunity(String communityName);

    @Select("""
            SELECT
                c.dep_id AS dep_id,
                pd.id AS person_id,
                pd.gender,
                pd.age,
                pd.dept_name,
                COALESCE(lhv.breath_rate, 0) AS breath_rate,
                COALESCE(lhv.systolic, 0) AS systolic,
                COALESCE(lhv.diastolic, 0) AS diastolic,
                COALESCE(lhv.blood_oxygen, 0) AS blood_oxygen,
                COALESCE(lhv.temperature, 0) AS temperature,
                COALESCE(lhv.heart_rate, 0) AS heart_rate,
                COALESCE(lhv.blood_glucose, 0) AS blood_glucose
            FROM person_data pd
            JOIN community c ON pd.dept_name = c.name
            LEFT JOIN latest_health_data_view lhv ON pd.id = lhv.researched_person_id
            WHERE c.name = #{communityName}
            """)
    List<Map<String, Object>> getDataByCommunityAll(String communityName);

    @Select("""
            SELECT
                IFNULL(lhdv.breath_rate, 0) AS breath_rate,
                IFNULL(lhdv.systolic, 0) AS systolic,
                IFNULL(lhdv.diastolic, 0) AS diastolic,
                IFNULL(lhdv.blood_oxygen, 0) AS blood_oxygen,
                IFNULL(lhdv.temperature, -999) AS p_temperature, 
                IFNULL(lhdv.heart_rate, 0) AS heart_rate,
                IFNULL(lhdv.blood_glucose, 0) AS blood_glucose,
                lhdv.create_time AS p_create_time,
                pd.gender,
                pd.age,
                pd.height,
                pd.weight,
                pd.bmi,
                pd.id,
                pd.dept_id,
                c.dep_id AS dep_id,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    0,
                    ROUND(ev.co, 2) 
                ) AS co,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    0,
                    ROUND(ev.pressure, 2)
                ) AS pressure,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    0,
                    ev.light
                ) AS light,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    0,
                    ROUND(ev.pm25, 2)
                ) AS pm25,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    0,
                    ROUND(ev.pm10, 2)
                ) AS pm10,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    0,
                    ROUND(ev.humidity, 2)
                ) AS humidity,
                IF(
                    ABS(TIMESTAMPDIFF(DAY, lhdv.create_time, ev.create_time)) > 7,
                    -999,
                    ROUND(ev.temperature, 2)
                ) AS e_temperature,
                DATE(ev.create_time) AS e_create_time
            FROM
                latest_health_data_view lhdv
            JOIN
                person_data pd ON pd.id = lhdv.researched_person_id
            JOIN
                community c ON c.id = pd.dept_id
            JOIN
                env_val ev ON ev.dept_id = c.dep_id
            WHERE
                lhdv.researched_person_id = #{id}
                AND pd.id = #{id}
            ORDER BY
                ABS(TIMESTAMPDIFF(SECOND, lhdv.create_time, ev.create_time))
            LIMIT 1;
            
            """)
    Map<String, Object> getPersonalHealthData(Integer id);

    @Select("""
            WITH LatestDate AS (
                SELECT MAX(create_time) AS max_date
                FROM env_val
            ),
            LightFrequency AS (
                SELECT
                    light,
                    COUNT(*) AS frequency
                FROM
                    env_val
                WHERE
                    create_time >= (SELECT DATE_SUB(max_date, INTERVAL 7 DAY) FROM LatestDate)
                    AND create_time <= (SELECT max_date FROM LatestDate)
                GROUP BY
                    light
                ORDER BY
                    frequency DESC
                LIMIT 1
            )
            SELECT
                COALESCE(ROUND(AVG(co), 2), 0) AS co,
                COALESCE(ROUND(AVG(pressure), 2), 0) AS pressure,
                COALESCE((SELECT light FROM LightFrequency), '') AS light,
                COALESCE(ROUND(AVG(pm25), 2), 0) AS pm25,
                COALESCE(ROUND(AVG(pm10), 2), 0) AS pm10,
                COALESCE(ROUND(AVG(humidity), 2), 0) AS humidity,
                COALESCE(ROUND(AVG(temperature), 2), -999) AS temperature,
                COALESCE(DATE(MAX(create_time)), '0') AS latest_date
            FROM
                env_val
            WHERE
                create_time >= (SELECT DATE_SUB(max_date, INTERVAL 7 DAY) FROM LatestDate)
                AND create_time <= (SELECT max_date FROM LatestDate);
            """)
    Map<String, Object> getEnvironmentData();


    @Select("""
            WITH LatestDate AS (
                SELECT MAX(create_time) AS max_date
                FROM env_val
                WHERE dept_id = #{dep_id}
            )
            SELECT
                COALESCE(ROUND(AVG(co), 2), 0) AS co,
                COALESCE(ROUND(AVG(pressure), 2), 0) AS pressure,
                COALESCE((
                    SELECT light
                    FROM env_val
                    WHERE dept_id = #{dep_id}
                    AND create_time >= (SELECT DATE_SUB(max_date, INTERVAL 7 DAY) FROM LatestDate)
                    AND create_time <= (SELECT max_date FROM LatestDate)
                    GROUP BY light
                    ORDER BY COUNT(*) DESC
                    LIMIT 1
                ), '0') AS light,
                COALESCE(ROUND(AVG(pm25), 2), 0) AS pm25,
                COALESCE(ROUND(AVG(pm10), 2), 0) AS pm10,
                COALESCE(ROUND(AVG(humidity), 2), 0) AS humidity,
                COALESCE(ROUND(AVG(temperature), 2), -999) AS temperature,
                COALESCE(DATE(MAX(create_time)), '0') AS latest_date
            FROM
                env_val
            WHERE
                dept_id = #{dep_id}
                AND create_time >= (SELECT DATE_SUB(max_date, INTERVAL 7 DAY) FROM LatestDate)
                AND create_time <= (SELECT max_date FROM LatestDate);
            """)
    Map<String, Object> getEnvironmentDataByCity(Integer dep_id);

    @Select("""
            WITH DeptID AS (
                SELECT dep_id
                FROM community
                WHERE name = #{cityName}
            ),
            RecentData AS (
                SELECT
                    ev.co,
                    ev.pressure,
                    ev.light,
                    ev.pm10,
                    ev.pm25,
                    ev.humidity,
                    ev.temperature,
                    ev.create_time
                FROM env_val AS ev
                JOIN DeptID AS d ON ev.dept_id = d.dep_id
                WHERE ev.create_time >= CURDATE() - INTERVAL 1 WEEK
            ),
            AggregatedData AS (
                SELECT
                    ROUND(AVG(co), 2) AS avg_co,
                    ROUND(AVG(pressure), 2) AS avg_pressure,
                    ROUND(AVG(pm10), 2) AS avg_pm10,
                    ROUND(AVG(pm25), 2) AS avg_pm25,
                    ROUND(AVG(humidity), 2) AS avg_humidity,
                    ROUND(AVG(temperature), 2) AS avg_temperature,
                    light
                FROM RecentData
                GROUP BY light
            ),
            MostFrequentLight AS (
                SELECT light
                FROM RecentData
                GROUP BY light
                ORDER BY COUNT(*) DESC
                LIMIT 1
            )
            SELECT
                ad.avg_co AS co,
                ad.avg_pressure AS pressure,
                mfl.light AS light,
                ad.avg_pm10 AS pm10,
                ad.avg_pm25 AS pm25,
                ad.avg_humidity AS humidity,
                ad.avg_temperature AS temperature
            FROM AggregatedData AS ad
            JOIN MostFrequentLight AS mfl
            LIMIT 1;
            """)
    Map<String, Object> getCommunityEnvironmentDataByCity(String cityName);

    @Select("""
            WITH selected_persons AS (
                SELECT id, gender, age, dept_name
                FROM person_data
                ORDER BY RAND()
                LIMIT 50
            ),
            dept_mapping AS (
                SELECT sp.id, sp.gender, sp.age, sp.dept_name, c.dep_id
                FROM selected_persons sp
                JOIN community c ON sp.dept_name = c.name
            )
            SELECT
                d.id AS person_id,
                d.gender,
                d.age,
                d.dept_name,
                d.dep_id,
                COALESCE(lhv.breath_rate, 0) AS breath_rate,
                COALESCE(lhv.systolic, 0) AS systolic,
                COALESCE(lhv.diastolic, 0) AS diastolic,
                COALESCE(lhv.blood_oxygen, 0) AS blood_oxygen,
                COALESCE(lhv.temperature, -999) AS temperature,
                COALESCE(lhv.heart_rate, 0) AS heart_rate,
                COALESCE(lhv.blood_glucose, 0) AS blood_glucose
            FROM dept_mapping d
            LEFT JOIN latest_health_data_view lhv ON d.id = lhv.researched_person_id;
            """)
    List<Map<String, Object>> getHealthDataRandomFifty();

    @Select("""
            SELECT
                #{id} AS dep_id,
                sp.id AS person_id,
                sp.gender,
                sp.age,
                sp.dept_name,
                COALESCE(lhv.breath_rate, 0) AS breath_rate,
                COALESCE(lhv.systolic, 0) AS systolic,
                COALESCE(lhv.diastolic, 0) AS diastolic,
                COALESCE(lhv.blood_oxygen, 0) AS blood_oxygen,
                COALESCE(lhv.temperature, 0) AS temperature,
                COALESCE(lhv.heart_rate, 0) AS heart_rate,
                COALESCE(lhv.blood_glucose, 0) AS blood_glucose
            FROM (
                SELECT id, gender, age, dept_name
                FROM person_data
                WHERE dept_name IN (
                    SELECT name
                    FROM community
                    WHERE dep_id = #{id}
                )
                ORDER BY RAND()
                LIMIT 50
            ) AS sp
            LEFT JOIN latest_health_data_view lhv ON sp.id = lhv.researched_person_id;
            """)
    List<Map<String, Object>> getHealthDataRandomFiftyByCity(Integer id);

    @Select("""
            SELECT
              ev.co,
              ev.pressure,
              ev.light,
              ev.pm10,
              ev.pm25,
              ev.humidity,
              ev.temperature,
              DATE(ev.create_time) AS latest_date
            FROM env_val AS ev
            WHERE dept_id IN (
                SELECT dep_id
                FROM community
                WHERE id IN (
                    SELECT dept_id
                    FROM person_data
                    WHERE id = #{id}
                )
            )
            ORDER BY create_time DESC
            LIMIT 1;
            
            """)
    Map<String, Object> getEnviromentByUserId(Integer id);


}
