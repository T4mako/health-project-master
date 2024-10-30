package com.keylab.healthproject.controller;

import com.keylab.healthproject.common.Result;
import com.keylab.healthproject.dao.PersonData;
import com.keylab.healthproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.keylab.healthproject.common.ResultCodeEnum.PARAM_ERROR;

@Validated
@RestController
@RequestMapping("/user")
public class userController {
    @Autowired
    private IUserService userService;
    //通过get方式根据用户id获取用户基本信息
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam Long id)    {
        PersonData personData=userService.getUserInfo(id);
        if(personData==null){
            return Result.error(String.valueOf(PARAM_ERROR),"用户不存在");
        }
        return Result.success(personData);
    }
}
