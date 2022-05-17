package com.www.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloWorldController {

    @RequestMapping("/")
    public String hello() {
        return "bar-race-git";
    }

    @ResponseBody
    @RequestMapping("/hello")
    public String helloWorld() {
        return "helloWorld";
    }

    @ResponseBody
    @RequestMapping("/data")
    public String jsonString() {
        return "[{\"demo\": 123}]";
    }
}
