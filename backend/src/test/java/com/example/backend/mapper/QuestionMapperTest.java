package com.example.backend.mapper;

import com.example.backend.model.entities.Question;
import com.example.backend.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest
class QuestionMapperTest {
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionService questionService;
    @Test
    public void test(){
        Question question = new Question();
        question.setId(1L);
        question.setTitle("cc");
        question.setTags("[\"sad\"]");
        question.setContent("ads");
//        System.out.println(question);
//        System.out.println(questionMapper.updateQuestionById(question));
        questionService.updateQuestion(question);
    }
}