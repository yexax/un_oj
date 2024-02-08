package com.example.backend.mapper;

import com.example.backend.model.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserMapperTest {
    @Resource
    private UserMapper userMapper;
    @Test
    public void test(){
        User user = new User();
        user.setUserAccount("123123");
        user.setPassword("123123");
        userMapper.insertUser(user);
    }
}