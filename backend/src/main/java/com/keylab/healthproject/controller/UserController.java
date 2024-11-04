package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.service.IPersonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author T4mako
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    IPersonDataService iPersonDataService;

    @GetMapping("/info")
        return Result.success(iPersonDataService.getById(id));
    }

    @GetMapping("/dayHData")
    public Result dayHData(@RequestParam long id) {
    }
}
