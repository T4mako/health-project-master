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
            WITH LatestDate AS (
                SELECT MAX(hd.create_time) AS max_date
                FROM health_data hd
                JOIN person_data pd ON hd.researched_person_id = pd.id
                JOIN community c ON pd.dept_id = c.id
                WHERE c.name = #{communityName}
            )
            
            SELECT
                pd.id AS person_id,
                pd.gender,
                pd.age,
                pd.dept_name,
                c.dep_id,
                COALESCE(FORMAT(AVG(hd.breath_rate), 1), 0) AS breath_rate,
                COALESCE(FORMAT(AVG(hd.systolic), 1), 0) AS systolic,
                COALESCE(FORMAT(AVG(hd.diastolic), 1), 0) AS diastolic,
                COALESCE(FORMAT(AVG(hd.blood_oxygen), 1), 0) AS blood_oxygen,
                COALESCE(FORMAT(AVG(hd.temperature), 1), 0) AS temperature,
                COALESCE(FORMAT(AVG(hd.heart_rate), 1), 0) AS heart_rate,
                COALESCE(FORMAT(AVG(hd.blood_glucose), 1), 0) AS blood_glucose
            FROM
                community c
            JOIN
                person_data pd ON c.id = pd.dept_id
            LEFT JOIN
                health_data hd ON pd.id = hd.researched_person_id
            WHERE
                c.name = #{communityName}
                AND hd.create_time >= DATE_SUB((SELECT max_date FROM LatestDate), INTERVAL 1 MONTH)
                AND hd.create_time < (SELECT max_date FROM LatestDate)
            GROUP BY
                pd.id, pd.gender, pd.age, pd.dept_name, c.dep_id;
            """)
    List<Map<String, Object>> getDataByCommunityAll(String communityName);

    @Select("""
            SELECT
                pd.id,
                pd.gender,
                pd.age,
                pd.height,
                pd.weight,
                pd.bmi,
                COALESCE(lh.breath_rate, 0) AS breath_rate,
                COALESCE(lh.systolic, 0) AS systolic,
                COALESCE(lh.diastolic, 0) AS diastolic,
                COALESCE(lh.blood_oxygen, 0) AS blood_oxygen,
                COALESCE(lh.temperature, -999) AS p_temperature,
                COALESCE(lh.heart_rate, 0) AS heart_rate,
                COALESCE(lh.blood_glucose, 0) AS blood_glucose,
                COALESCE(lh.create_time, '0') AS p_create_time,
                COALESCE(ev.co, 0) AS co,
                COALESCE(ev.pressure, 0) AS pressure,
                COALESCE(ev.light, '0') AS light,
                COALESCE(ev.pm25, 0) AS pm25,
                COALESCE(ev.pm10, 0) AS pm10,
                COALESCE(ev.humidity, 0) AS humidity,
                COALESCE(ev.temperature, -999) AS e_temperature,
                COALESCE(ev.create_time, '0') AS e_create_time
            FROM
                person_data pd
            LEFT JOIN
                latest_health_data_view lh ON pd.id = lh.researched_person_id
            LEFT JOIN
                env_val ev ON pd.family_user_id = ev.family_user_id
                AND ev.create_time = (
                    SELECT
                        create_time
                    FROM
                        env_val
                    WHERE
                        family_user_id = pd.family_user_id
                        AND ABS(DATEDIFF(ev.create_time, lh.create_time)) <= 7
                    ORDER BY
                        ABS(DATEDIFF(ev.create_time, lh.create_time))
                    LIMIT 1
                )
            WHERE
                pd.id = #{id};
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
            WITH latest_data AS (
                SELECT
                    e.*,
                    ROW_NUMBER() OVER (PARTITION BY e.family_user_id ORDER BY e.create_time DESC) AS rn
                FROM
                    env_val e
                INNER JOIN
                    person_data p ON e.family_user_id = p.family_user_id
                WHERE
                    p.dept_name IN (SELECT dept_name FROM person_data WHERE dept_name = #{cityName})
                    AND e.create_time >= DATE_SUB((SELECT MAX(create_time) FROM env_val), INTERVAL 7 DAY)
            )
            SELECT
                CASE WHEN COUNT(*) > 0 THEN ROUND(AVG(co), 2) ELSE NULL END AS co,
                CASE WHEN COUNT(*) > 0 THEN ROUND(AVG(pressure), 2) ELSE NULL END AS pressure,
                CASE WHEN COUNT(*) > 0 THEN ROUND(AVG(pm25), 2) ELSE NULL END AS pm25,
                CASE WHEN COUNT(*) > 0 THEN ROUND(AVG(pm10), 2) ELSE NULL END AS pm10,
                CASE WHEN COUNT(*) > 0 THEN ROUND(AVG(humidity), 2) ELSE NULL END AS humidity,
                CASE WHEN COUNT(*) > 0 THEN ROUND(AVG(temperature), 2) ELSE NULL END AS temperature,
                (SELECT light FROM (
                    SELECT light, COUNT(*) AS cnt
                    FROM latest_data
                    GROUP BY light
                    ORDER BY cnt DESC
                    LIMIT 1
                ) AS light_freq) AS light
            FROM
                latest_data
            WHERE
                rn = 1;
            """)
    Map<String, Object> getCommunityEnvironmentDataByCity(String cityName);

    @Select("""
            WITH LatestDate AS (
                SELECT MAX(hd.create_time) AS max_date
                FROM health_data hd
            )
            
            SELECT
                pd.id AS person_id,
                pd.gender,
                pd.age,
                pd.dept_name,
                c.dep_id,
                FORMAT(COALESCE(AVG(hd.breath_rate), 0), 1) AS breath_rate,
                FORMAT(COALESCE(AVG(hd.systolic), 0), 1) AS systolic,
                FORMAT(COALESCE(AVG(hd.diastolic), 0), 1) AS diastolic,
                FORMAT(COALESCE(AVG(hd.blood_oxygen), 0), 1) AS blood_oxygen,
                FORMAT(COALESCE(AVG(hd.temperature), 0), 1) AS temperature,
                FORMAT(COALESCE(AVG(hd.heart_rate), 0), 1) AS heart_rate,
                FORMAT(COALESCE(AVG(hd.blood_glucose), 0), 1) AS blood_glucose
            FROM
                community c
            JOIN
                person_data pd ON c.id = pd.dept_id
            LEFT JOIN
                health_data hd ON pd.id = hd.researched_person_id
            WHERE
                hd.create_time >= DATE_SUB((SELECT max_date FROM LatestDate), INTERVAL 1 MONTH)
                AND hd.create_time < (SELECT max_date FROM LatestDate)
            GROUP BY
                pd.id, pd.gender, pd.age, pd.dept_name, c.dep_id
            ORDER BY
                RAND()
            LIMIT 50;
            """)
    List<Map<String, Object>> getHealthDataRandomFifty();

    @Select("""
            WITH LatestDate AS (
                SELECT MAX(hd.create_time) AS max_date
                FROM health_data hd
                JOIN person_data pd ON hd.researched_person_id = pd.id
                JOIN community c ON pd.dept_id = c.id
                WHERE c.dep_id = #{id}
            ),
            RandomIDs AS (
                SELECT pd.id
                FROM person_data pd
                JOIN community c ON c.id = pd.dept_id
                WHERE c.dep_id = #{id}
                LIMIT 50
            )
            SELECT
                pd.id AS person_id,
                pd.gender,
                pd.age,
                pd.dept_name,
                c.dep_id,
                FORMAT(COALESCE(AVG(hd.breath_rate), 0), 1) AS breath_rate,
                FORMAT(COALESCE(AVG(hd.systolic), 0), 1) AS systolic,
                FORMAT(COALESCE(AVG(hd.diastolic), 0), 1) AS diastolic,
                FORMAT(COALESCE(AVG(hd.blood_oxygen), 0), 1) AS blood_oxygen,
                FORMAT(COALESCE(AVG(hd.temperature), 0), 1) AS temperature,
                FORMAT(COALESCE(AVG(hd.heart_rate), 0), 1) AS heart_rate,
                FORMAT(COALESCE(AVG(hd.blood_glucose), 0), 1) AS blood_glucose
            FROM
                RandomIDs ri
            JOIN person_data pd ON ri.id = pd.id
            LEFT JOIN health_data hd ON pd.id = hd.researched_person_id
            LEFT JOIN community c ON c.id = pd.dept_id
            WHERE
                hd.create_time >= DATE_SUB((SELECT max_date FROM LatestDate), INTERVAL 1 MONTH)
                AND hd.create_time < (SELECT max_date FROM LatestDate)
            GROUP BY
                pd.id, pd.gender, pd.age, pd.dept_name, c.dep_id;
            """)
    List<Map<String, Object>> getHealthDataRandomFiftyByCity(Integer id);

    @Select("""
            SELECT
                COALESCE(e.co, 0) AS co,
                COALESCE(e.pressure, 0) AS pressure,
                COALESCE(e.light, '0') AS light,
                COALESCE(e.pm25, 0) AS pm25,
                COALESCE(e.pm10, 0) AS pm10,
                COALESCE(e.humidity, 0) AS humidity,
                COALESCE(e.temperature, -999) AS temperature,
                DATE_FORMAT(COALESCE(e.create_time, NOW()), '%Y-%m-%d') AS latest_date
            FROM
                env_val e
            JOIN
                person_data p ON e.family_user_id = p.family_user_id
            WHERE
                p.id = #{id}
            ORDER BY
                e.create_time DESC
            LIMIT 1;
            """)
    Map<String, Object> getEnviromentByUserId(Integer id);
}
