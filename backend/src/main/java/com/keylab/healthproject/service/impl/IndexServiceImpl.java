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
}
