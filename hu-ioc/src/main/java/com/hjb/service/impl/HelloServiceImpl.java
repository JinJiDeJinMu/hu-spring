package com.hjb.service.impl;

import com.hjb.annotation.HuAutowired;
import com.hjb.annotation.HuService;
import com.hjb.service.HelloService;
import com.hjb.service.UserService;

@HuService
public class HelloServiceImpl implements HelloService {

    @HuAutowired
    private UserService userServiceImpl;

    @Override
    public String hello() {
        System.out.println(userServiceImpl.test());
        return "Just do it";
    }
}
