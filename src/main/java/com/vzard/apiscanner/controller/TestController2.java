package com.vzard.apiscanner.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        path = "v1/test2"
)

@RestController
public class TestController2 {

    //测试ur中“/”的省缺问题
    @RequestMapping(value = "post-test",method = RequestMethod.POST)
    public void postTest(){};

    @RequestMapping(value = "/post-test",method = RequestMethod.POST)
    public void postTest(String s){};



}
