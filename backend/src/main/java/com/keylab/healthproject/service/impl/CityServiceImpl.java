package com.keylab.healthproject.service.impl;

import com.keylab.healthproject.mapper.CityMapper;
import com.keylab.healthproject.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements ICityService {
    @Autowired
    private CityMapper cityMapper;

    @Override
    public List<Map<String, Object>> getCityNameAndNum() {
        return cityMapper.getCityNameAndNum();
    }

    @Override
    public long getNumByCityName(String cityName) {
        if(cityName.equals("西安市") ||cityName.equals("陕西省")){
            return cityMapper.getNumByCityId(12);
        } else if (cityName.equals("郑州市")||cityName.equals("河南省")) {
            return cityMapper.getNumByCityId(11);
        }else if (cityName.equals("徐州市")||cityName.equals("江苏省"))
            return cityMapper.getNumByCityId(10);
        else
            return 0;

    }
}
