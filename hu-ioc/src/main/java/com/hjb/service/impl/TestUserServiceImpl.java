package com.hjb.service.impl;

import com.hjb.annotation.HuService;
import com.hjb.service.UserService;

public class TestUserServiceImpl implements UserService {
    @Override
    public String hello() {
        return "hello test";
    }

    @Override
    public String test() {
        return null;
    }
}
