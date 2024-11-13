package com.keylab.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keylab.healthproject.dao.EnvVal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author T4mako
 * @since 2024-10-24
 */
@Mapper
public interface EnvValMapper extends BaseMapper<EnvVal> {
    @Select("SELECT e.* FROM env_val e " +
            "JOIN person_data p ON e.family_user_id = p.family_user_id " +
            "WHERE p.id = #{id} " +
            "AND DATE(e.create_time) = CURDATE()")
    List<EnvVal> findTodayEnvDataByFamilyId(long id);
}
