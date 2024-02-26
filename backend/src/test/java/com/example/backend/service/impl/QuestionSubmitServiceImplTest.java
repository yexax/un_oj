package com.example.backend.service.impl;

import com.example.backend.model.entities.Question;
import com.example.backend.model.entities.QuestionSubmit;
import com.example.backend.service.QuestionSubmitService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class QuestionSubmitServiceImplTest {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Test
    void test(){
        QuestionSubmit questionSubmit = questionSubmitService.getById(1L);
        System.out.println(questionSubmit);
    }
}