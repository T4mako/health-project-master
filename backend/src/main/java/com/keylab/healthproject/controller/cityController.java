package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.service.IIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/city")
public class cityController {
    @Autowired
    private IIndexService indexService;

    //查询所有城市对应名称及人数
    @RequestMapping("/getCityNameAndNum")
    public Result getCityNameAndNum(){
        List<Map<String, Object>> lisMap= indexService.getCityNameAndNum();
        return Result.success(lisMap);
    }

    //根据城市名查询对应人数
    @RequestMapping("/getNumByCityName")
    public Result getNumByCityName(String cityName){
        long num= indexService.getNumByCityName(cityName);
        return Result.success(num);
    }

}
