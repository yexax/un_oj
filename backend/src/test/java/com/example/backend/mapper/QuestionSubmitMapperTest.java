package com.example.backend.mapper;

import com.example.backend.model.entities.QuestionSubmit;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class QuestionSubmitMapperTest {
    @Resource
    private QuestionSubmitMapper questionSubmitMapper;
    @Test
    public void test(){
//        System.out.println(questionSubmitMapper.listQuestionSubmit(0, 10));
//        System.out.println(questionSubmitMapper.getCount());
        QuestionSubmit questionSubmit = new QuestionSubmit(null, "java", "asd", "asd", null, 1L, 1L, null, null, 0);
        questionSubmitMapper.questionSubmit(questionSubmit);
        System.out.println(questionSubmit.getId());
    }
    @Test
    void test2(){
        System.out.println(questionSubmitMapper.updateStatus(1, "Accepted"));
    }
}