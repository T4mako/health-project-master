package com.keylab.healthproject.mapper;

import com.keylab.healthproject.dao.PersonData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    // 获取用户信息，根据id
    @Select("SELECT * FROM person_data WHERE id = #{id}")
    PersonData getUserInfo(Long id);
}
