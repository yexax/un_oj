package com.example.backend.service;


import com.example.backend.model.dto.question.QuestionUpdateRequest;
import com.example.backend.model.entities.Question;
import com.example.backend.model.vo.QuestionPageVO;

import java.util.List;

public interface QuestionService {
    Boolean addQuestion(Question question);
    Question getQuestionById(Long id);
    Boolean deleteQuestion(Long id);
    Boolean updateQuestion(Question question);
    QuestionPageVO listQuestionByPage(Integer page, Integer pageSize);
    QuestionPageVO searchQuestionByPage(Integer page,Integer pageSize,String title,String tags);
}
