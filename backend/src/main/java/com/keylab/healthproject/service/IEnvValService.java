package com.keylab.healthproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.keylab.healthproject.dao.EnvVal;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author T4mako
 * @since 2024-10-24
 */
public interface IEnvValService extends IService<EnvVal> {
    EnvVal getTodayEnvDataByUserId(long id);

    // 插入环境数据
    void saveEnvVal(EnvVal envVal);
}
