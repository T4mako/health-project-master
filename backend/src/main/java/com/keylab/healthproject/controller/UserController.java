package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.service.IHealthDataService;
import com.keylab.healthproject.service.IPersonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author T4mako
 * @date 2024/10/31 9:21
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IPersonDataService iPersonDataService;

    @Autowired
    IHealthDataService iHealthDataService;

    @GetMapping("/info")
    public Result userInfo(@RequestParam long id) {
        return Result.success(iPersonDataService.getById(id));
    }

    @GetMapping("/dayHData")
    public Result dayHData(@RequestParam long id) {
        return Result.success(iHealthDataService.dayHData(id));
    }


}