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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

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
        EnvVal envVal = null;
        // 根据用户所在城市获取最新的数据
        LambdaQueryWrapper<PersonData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PersonData::getId, id);
        PersonData personData = personDataMapper.selectOne(queryWrapper);
        long deptId = personData.getDeptId();
        // 查询城市最新环境信息
        LambdaQueryWrapper<EnvVal> envValLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 徐州
        if(deptId == 13 || deptId == 26 || deptId == 27 || deptId == 28) {
            envValLambdaQueryWrapper.eq(EnvVal::getDeptId, 10).orderByDesc(EnvVal::getCreateTime).last("limit 1");
            envVal = envValMapper.selectOne(envValLambdaQueryWrapper);
        }else if(deptId == 16 || deptId == 17 || deptId == 18 || deptId == 19){
            // 郑州
            envValLambdaQueryWrapper.eq(EnvVal::getDeptId, 11).orderByDesc(EnvVal::getCreateTime).last("limit 1");
            envVal = envValMapper.selectOne(envValLambdaQueryWrapper);
        }else {
            // 西安
            envValLambdaQueryWrapper.eq(EnvVal::getDeptId, 12).orderByDesc(EnvVal::getCreateTime).last("limit 1");
            envVal = envValMapper.selectOne(envValLambdaQueryWrapper);
        }
        return envVal;
    }

    @Override
    public void saveEnvVal(EnvVal envVal) {
        // 设置UUID主键
        envVal.setId(UUID.randomUUID().toString());

        // 设置创建时间
        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        envVal.setCreateTime(LocalDateTime.now().format(formatter));

        // 调用 MyBatis-Plus 提供的插入方法
        this.save(envVal);
    }
}
