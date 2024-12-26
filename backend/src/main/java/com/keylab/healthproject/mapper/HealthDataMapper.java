package com.keylab.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keylab.healthproject.dao.HealthData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author T4mako
 * @since 2024-10-24
 */
@Mapper
public interface HealthDataMapper extends BaseMapper<HealthData> {

    @Select("select hd.researched_person_id ,hd.breath_rate,pd.age  \n" +
            "from health_data hd \n" +
            "right join person_data pd \n" +
            "ON hd.researched_person_id = pd.id \n" +
            "where hd.create_time = '2024-10-31'")
    List<Map<String, Object>> selectAllCompareData(String indicator);

    @Select("""
        SELECT 
            hd.researched_person_id AS id,
            hd.${indicator},
            pd.age 
        FROM 
            health.latest_health_data_view hd
        JOIN 
            health.person_data pd ON hd.researched_person_id = pd.id
        WHERE 
            pd.dept_name = (SELECT dept_name FROM health.person_data WHERE id = #{id})
        AND 
            hd.${indicator} IS NOT NULL
        """)
    List<Map<String, Object>> selectAllCommunityCompareData(long id, String indicator);


    @Select("""
        SELECT
            hd.researched_person_id AS id,
            hd.${indicator},
            pd.age
        FROM
            health.latest_health_data_view hd
        RIGHT JOIN
            health.person_data pd ON hd.researched_person_id = pd.id
        WHERE
            pd.dept_name IN (
                SELECT
                    name
                FROM
                    community
                WHERE
                    dep_id IN (
                        SELECT
                            dep_id
                        FROM
                            community c,
                            person_data p
                        WHERE
                            p.dept_id = c.id
                            AND p.id = #{id}
                    )
            )
        AND
            hd.${indicator} IS NOT NULL
        """)
    List<Map<String, Object>> selectAllProvinceCompareData(long id, String indicator);



    @Select("SELECT hd.researched_person_id, hd.${indicator}, pd.age\n" +
            "FROM health_data hd\n" +
            "RIGHT JOIN person_data pd\n" +
            "ON hd.researched_person_id = pd.id\n" +
            "WHERE hd.researched_person_id = #{id}\n" +
            "AND hd.${indicator} IS NOT NULL\n" +
            "ORDER BY hd.create_time DESC\n" +
            "LIMIT 1;")
    List<Map<String, Object>> getAgeIndicator(long id,String indicator);

    @Select("SELECT \n" +
            "    pd.id,\n" +
            "    pd.gender,\n" +
            "    pd.age,\n" +
            "    pd.height,\n" +
            "    pd.weight,\n" +
            "    pd.bmi,\n" +
            "    lhda.breath_rate,\n" +
            "    lhda.systolic,\n" +
            "    lhda.diastolic,\n" +
            "    lhda.blood_oxygen,\n" +
            "    lhda.temperature,\n" +
            "    lhda.heart_rate,\n" +
            "    lhda.blood_glucose\n" +
            "FROM \n" +
            "    person_data pd\n" +
            "LEFT JOIN \n" +
            "    latest_health_data_with_avg lhda\n" +
            "ON \n" +
            "    pd.id = lhda.researched_person_id\n" +
            "WHERE \n" +
            "    pd.dept_id = (SELECT dept_id FROM person_data WHERE id = #{id})\n" +
            "    AND lhda.heart_rate IS NOT NULL\n" +
            "    AND lhda.blood_glucose IS NOT NULL\n" +
            "    AND lhda.blood_oxygen IS NOT NULL\n" +
            "LIMIT 20;")
    List<Map<String, Object>> communityAllInfo(long id);

    @Select("SELECT \n" +
            "    pd.id,\n" +
            "    pd.gender,\n" +
            "    pd.age,\n" +
            "    pd.height,\n" +
            "    pd.weight,\n" +
            "    pd.bmi,\n" +
            "    COALESCE(lhda.breath_rate, (SELECT AVG(breath_rate) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS breath_rate,\n" +
            "    COALESCE(lhda.systolic, (SELECT AVG(systolic) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS systolic,\n" +
            "    COALESCE(lhda.diastolic, (SELECT AVG(diastolic) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS diastolic,\n" +
            "    COALESCE(lhda.blood_oxygen, (SELECT AVG(blood_oxygen) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS blood_oxygen,\n" +
            "    COALESCE(lhda.temperature, (SELECT AVG(temperature) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS temperature,\n" +
            "    COALESCE(lhda.heart_rate, (SELECT AVG(heart_rate) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS heart_rate,\n" +
            "    COALESCE(lhda.blood_glucose, (SELECT AVG(blood_glucose) FROM health_data hd JOIN person_data pd2 ON hd.researched_person_id = pd2.id WHERE pd2.dept_id = pd.dept_id)) AS blood_glucose\n" +
            "FROM \n" +
            "    person_data pd\n" +
            "LEFT JOIN \n" +
            "    latest_health_data_with_avg lhda ON pd.id = lhda.researched_person_id\n" +
            "WHERE \n" +
            "    pd.id = #{id}  -- 替换为实际用户的 ID\n" +
            "LIMIT 1;\n")
    List<Map<String, Object>> getLatestFullHData(long id);

    @Select("SELECT \n" +
            "    researched_person_id,\n" +
            "    breath_rate,\n" +
            "    systolic,\n" +
            "    diastolic,\n" +
            "    blood_oxygen,\n" +
            "    temperature,\n" +
            "    heart_rate,\n" +
            "    blood_glucose\n" +
            "FROM \n" +
            "    health.latest_health_data_with_avg\n" +
            "WHERE \n" +
            "    researched_person_id = #{id};")
    List<HealthData> getLatestHData(long id);

    @Update("UPDATE health_data\n" +
            "SET \n" +
            "    temperature = NULLIF(temperature, 0),\n" +
            "    heart_rate = NULLIF(heart_rate, 0),\n" +
            "    breath_rate = NULLIF(breath_rate, 0),\n" +
            "    blood_oxygen = NULLIF(blood_oxygen, 0),\n" +
            "    blood_glucose = NULLIF(blood_glucose, 0),\n" +
            "    systolic = NULLIF(systolic, 0),\n" +
            "    diastolic = NULLIF(diastolic, 0)\n" +
            "WHERE \n" +
            "    temperature = 0 OR\n" +
            "    heart_rate = 0 OR\n" +
            "    breath_rate = 0 OR\n" +
            "    blood_oxygen = 0 OR\n" +
            "    blood_glucose = 0 OR\n" +
            "    systolic = 0 OR\n" +
            "    diastolic = 0;")
    void zeroToNull();
}
