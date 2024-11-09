package com.keylab.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keylab.healthproject.dao.HealthData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    @Select("select researched_person_id ,${indicator},create_time from health_data hd " +
            "where create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) ")
    List<Map<String, Object>> selectAllCompareData(String indicator);

    @Select("SELECT \n" +
            "    hd.researched_person_id AS id, \n" +
            "    hd.${indicator}, \n" +
            "    hd.create_time \n" +
            "FROM \n" +
            "    health_data hd \n" +
            "RIGHT JOIN \n" +
            "    person_data pd ON hd.researched_person_id = pd.id\n" +
            "WHERE \n" +
            "    hd.create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
            "AND \n" +
            "    pd.dept_name = (SELECT dept_name FROM person_data WHERE id = #{id});")
    List<Map<String, Object>> selectAllCommunityCompareData(long id, String indicator);


    @Select("SELECT\n" +
            "   hd.researched_person_id AS id, \n" +
            "   hd.${indicator} , \n" +
            "   hd.create_time  \n" +
            "FROM \n" +
            "   health_data hd \n" +
            "RIGHT JOIN \n" +
            "   person_data pd ON hd.researched_person_id = pd.id\n" +
            "WHERE \n" +
            "   hd.create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)\n" +
            "AND \n" +
            "   pd.dept_name in (\n" +
            "select name from community where dep_id  " +
            "in (select dep_id from community c,person_data p where p.dept_id = c.id and p.id = #{id}));")
    List<Map<String, Object>> selectAllProvinceCompareData(long id, String indicator);

    @Select("SELECT * FROM health_data WHERE researched_person_id = #{id} AND create_time = CURDATE()")
    HealthData findByResearchedPersonId(long id);
}
