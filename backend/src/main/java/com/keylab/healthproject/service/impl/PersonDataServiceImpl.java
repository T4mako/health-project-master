package com.keylab.healthproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.mapper.PersonDataMapper;
import com.keylab.healthproject.service.IPersonDataService;
import org.springframework.stereotype.Service;

/**
 * @author T4mako
 * @date 2024/10/29 16:03
 */
@Service
public class PersonDataServiceImpl extends ServiceImpl<PersonDataMapper, PersonData> implements IPersonDataService {
}
