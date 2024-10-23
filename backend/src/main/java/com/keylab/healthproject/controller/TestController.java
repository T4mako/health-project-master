package com.keylab.healthproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author T4mako
 * @date 2024/10/23 16:11
 */
@RestController
@RequestMapping("/")
public class TestController {
    @GetMapping
    public String test(){
        return "Hello World";
    }
}
