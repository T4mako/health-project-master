package com.keylab.healthproject.service.impl;

import com.keylab.healthproject.mapper.IndexMapper;
import com.keylab.healthproject.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class IndexServiceImpl implements IIndexService {
    @Autowired
    private IndexMapper indexMapper;

    @Override
    public List<Map<String, Object>> getCityNameAndNum() {
        return indexMapper.getCityNameAndNum();
    }

    @Override
    public long getNumByCityName(String cityName) {
        if(cityName.equals("西安市") ||cityName.equals("陕西省")){
            return indexMapper.getNumByCityId(12);
        } else if (cityName.equals("郑州市")||cityName.equals("河南省")) {
            return indexMapper.getNumByCityId(11);
        }else if (cityName.equals("徐州市")||cityName.equals("江苏省"))
            return indexMapper.getNumByCityId(10);
        else
            return 0;

    }
}
