package com.vzard.apiscanner.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(
        path = "v1/test/"
)
@RestController
public class TestController1 {

    @RequestMapping(value = "/get-test",method = RequestMethod.GET)
    public void getTest(){};
    //测试同名不同参数
    @RequestMapping(value = "/get-test",method = RequestMethod.GET)
    public void getTest(String s){};





}
