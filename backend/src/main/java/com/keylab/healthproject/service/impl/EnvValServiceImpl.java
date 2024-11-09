package com.keylab.healthproject.service.impl;

import com.keylab.healthproject.dao.EnvVal;
import com.keylab.healthproject.mapper.EnvValMapper;
import com.keylab.healthproject.mapper.PersonDataMapper;
import com.keylab.healthproject.service.IEnvValService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author T4mako
 * @since 2024-10-24
 */
@Service
public class EnvValServiceImpl extends ServiceImpl<EnvValMapper, EnvVal> implements IEnvValService {
    @Autowired
    private EnvValMapper envValMapper;

    @Autowired
    private PersonDataMapper personDataMapper;

    public List<EnvVal> getTodayEnvDataByUserId(long id) {
        return envValMapper.findTodayEnvDataByFamilyId(id);
    }
}
