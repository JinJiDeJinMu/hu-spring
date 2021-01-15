package com.hjb.service.impl;

import com.hjb.annotation.HuAutowired;
import com.hjb.annotation.HuService;
import com.hjb.service.HelloService;
import com.hjb.service.UserService;

@HuService
public class UserServiceImpl implements UserService {

    @HuAutowired
    private HelloService helloServiceImpl;

    @Override
    public String hello() {
        System.out.println(helloServiceImpl.hello());
        return "hello world";
    }

    @Override
    public String test() {
        return "test test";
    }
}
