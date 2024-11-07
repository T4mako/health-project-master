package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.dao.EnvVal;
import com.keylab.healthproject.service.IEnvValService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author T4mako
 * @date 2024/10/31 20:48
 */
@RestController
@RequestMapping("/env")
public class EnvController {
    @Autowired
    IEnvValService iEnvValServicel;

    @GetMapping("/all")
    public Result getAllEnvVal() {
        List<EnvVal> list = iEnvValServicel.list();
        return Result.success(list);
    }
}
