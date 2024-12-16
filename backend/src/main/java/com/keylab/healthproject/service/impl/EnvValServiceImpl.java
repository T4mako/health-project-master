package com.keylab.healthproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.keylab.healthproject.dao.EnvVal;
import com.keylab.healthproject.dao.PersonData;
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

    public EnvVal getTodayEnvDataByUserId(long id) {
        // 根据 用户id 获取 家庭 id
        LambdaQueryWrapper<PersonData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersonData::getId,id);
        PersonData personData = personDataMapper.selectOne(queryWrapper);
        long familyUserId = personData.getFamilyUserId();
        LambdaQueryWrapper<EnvVal> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(EnvVal::getFamilyUserId, familyUserId);
        queryWrapper2.orderByDesc(EnvVal::getCreateTime);
        List<EnvVal> envVal = envValMapper.selectList(queryWrapper2);
        if(envVal.size()>0){
            EnvVal envVal1 = envVal.get(0);
            return envVal1;
        }
        return null;
    }
}
