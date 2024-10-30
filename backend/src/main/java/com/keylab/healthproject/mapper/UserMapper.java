package com.keylab.healthproject.mapper;

import com.keylab.healthproject.dao.HealthData;
import com.keylab.healthproject.dao.PersonData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 获取用户信息，根据id
    @Select("SELECT * FROM person_data WHERE id = #{id}")
    PersonData getUserInfo(Long id);

    // 获取用户健康信息，根据id
    @Select("SELECT * FROM health_data WHERE id = #{id}")
    HealthData getUserHealthInfo(Long id);
}
