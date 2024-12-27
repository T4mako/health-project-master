package com.keylab.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.keylab.healthproject.dao.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author T4mako
 * @date 2024/12/25 23:23
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
