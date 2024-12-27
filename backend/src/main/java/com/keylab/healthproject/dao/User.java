package com.keylab.healthproject.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author T4mako
 * @date 2024/12/25 23:17
 */
@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO) // 声明 id 为自增主键
    private Long id;
    private String username;
    private String password;
}
