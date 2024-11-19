package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.common.ResultCodeEnum;
import com.keylab.healthproject.service.ICityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/city")
public class cityController {
    @Autowired
    private ICityService indexService;

    //查询所有城市对应名称及人数
    @RequestMapping("/getCityNameAndNum")
    public Result getCityNameAndNum() {
        List<Map<String, Object>> lisMap = indexService.getCityNameAndNum();
        return Result.success(lisMap);
    }

    //根据城市名查询对应人数
    @RequestMapping("/getNumByCityName")
    public Result getNumByCityName(@RequestParam String cityName) {
        if (cityName.equals("中国")) {
            List<Map<String, Object>> lisMap = indexService.getCityNameAndNum();
            return Result.success(lisMap);
        } else {
            long num = indexService.getNumByCityName(cityName);
            if (num == 0)
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            return Result.success(num);
        }
    }

    // 查询所有城市，返回男女数量比例
    @RequestMapping("/getSexCount")
    public Result getSexCount() {
        List<Map<String, Object>> list = indexService.getSexCount();
        return Result.success(list);
    }

    // 根据城市名称，返回该城市的男女数量比例
    @RequestMapping("/getSexCountByCity")
    public Result getSexCountByCity(@RequestParam String cityName) {
        List<Map<String, Object>> list;
        if (cityName.equals("中国")) {
            list = indexService.getSexCount();
        } else {
            list = indexService.getSexCountByCity(cityName);
            if (list == null)
                return Result.error(ResultCodeEnum.PARAM_ERROR);
        }
        return Result.success(list);
    }

    // 返回各个省所有社区健康情况累加和
    @RequestMapping("/getHealthStatus")
    public Result getHealthStatus() {
        List<Map<String, Object>> list = indexService.getHealthStatus();
        return Result.success(list);
    }

    //根据城市名称，返回小区健康情况
    @RequestMapping("/getHealthStatusByCity")
    public Result getHealthStatusByCity(@RequestParam String cityName) {
        if (cityName.equals("中国")) {
            List<Map<String, Object>> list = indexService.getHealthStatus();
            return Result.success(list);
        } else if (cityName.isEmpty()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } else {
            List<Map<String, Object>> list = indexService.getHealthStatusByCity(cityName);
            if (list == null) {
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            }
            return Result.success(list);
        }
    }

    // 返回所有城市健康水平风险数量
    @RequestMapping("/getHealthLevel")
    public Result getHealthLevel() {
        List<Map<String, Object>> list = indexService.getHealthLevel();
        return Result.success(list);
    }

    // 返回指定城市的健康水平风险数量
    @RequestMapping("/getHealthLevelByCity")
    public Result getHealthLevelByCity(@RequestParam @Validated String cityName) {
        if (cityName.equals("中国")) {
            List<Map<String, Object>> list = indexService.getHealthLevel();
            return Result.success(list);
        } else if (cityName.isEmpty()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } else {
            List<Map<String, Object>> list = indexService.getHealthLevelByCity(cityName);
            if (list == null) {
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            }
            return Result.success(list);
        }
    }

    //返回指定小区相关健康数据
    @RequestMapping("/getHealthDataByCommunity")
    public Result getHealthDataByCommunity(@RequestParam String communityName) {
        if (communityName.isEmpty()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } else {
            Map<String, Object> map = indexService.getHealthDataByCommunity(communityName);
            if (map == null) {
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            }
            return Result.success(map);
        }
    }

    // 返回小区全员健康信息
    @RequestMapping("/getHealthDataByCommunityAll")
    public Result getDataByCommunityAll(@RequestParam String communityName) {
        if (communityName.isEmpty()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } else {
            List<Map<String, Object>> list = indexService.getDataByCommunityAll(communityName);
            if (list.isEmpty()) {
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            }
            return Result.success(list);
        }
    }

    //返回个人健康信息及环境数据
    @RequestMapping("/getPersonalHealthData")
    public Result getPersonalHealthData(@RequestParam Integer id) {
        if (id == null) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } else {
            Map<String, Object> map = indexService.getPersonalHealthData(id);
            if (map == null) {
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            }
            return Result.success(map);
        }
    }

    //查询所有环境数据，返回近一个周的环境平均值
    @RequestMapping("/getEnvironmentData")
    public Result getEnvironmentData() {
        Map<String, Object> map = indexService.getEnvironmentData();
        return Result.success(map);
    }

    // 返回指定城市的一个周环境数据平均值
    @RequestMapping("/getEnvironmentDataByCity")
    public Result getEnvironmentDataByCity(@RequestParam String cityName) {
        if (cityName.equals("中国")) {
            Map<String, Object> map = indexService.getEnvironmentData();
            return Result.success(map);
        } else if (cityName.isEmpty()) {
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        } else {
            Map<String, Object> map = indexService.getEnvironmentDataByCity(cityName);
            if (map == null) {
                return Result.error(ResultCodeEnum.PARAM_ERROR);
            }
            return Result.success(map);
        }
    }
    // 返回指定小区的健康数据，返回近一个周的数据
    @RequestMapping("/getCommunityEnvironmentDataByCity")
    public Result getCommunityEnvironmentDataByCity(@RequestParam String communityName) {
        if (communityName.isEmpty())
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        Map<String, Object> map = indexService.getCommunityEnvironmentDataByCity(communityName);
        if (map == null)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        return Result.success(map);
    }

    //返回指定城市的健康数据，根据市名
    @RequestMapping("/getHealthDataAllByCityName")
    public Result getHealthDataAllByCityName(@RequestParam String cityName) {
        List<Map<String, Object>> list = indexService.getHealthDataAllByCityName(cityName);
        if (list == null)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        return Result.success(list);
    }

    //返回全国或整省所有健康信息
    @RequestMapping("/getHealthDataAll")
    public Result getHealthDataAll() {
        List<Map<String, Object>> list = indexService.getHealthDataAll();
        if (list == null)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        return Result.success(list);
    }

    //根据用户id，返回该用户的环境数据
    @RequestMapping("/getEnviromentByUserId")
    public Result getEnviromentByUserId(@RequestParam Integer Id) {
        if (Id == null)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        Map<String, Object> map = indexService.getEnviromentByUserId(Id);
        if (map == null)
            return Result.error(ResultCodeEnum.PARAM_ERROR);
        return Result.success(map);
    }
}
