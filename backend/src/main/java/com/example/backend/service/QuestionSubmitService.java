package com.example.backend.service;

import com.example.backend.judge.model.JudgeResponseEnum;
import com.example.backend.model.dto.question_submit.QuestionSubmitRequest;
import com.example.backend.model.entities.QuestionSubmit;
import com.example.backend.model.vo.QuestionSubmitPageVO;

import javax.servlet.http.HttpServletRequest;

public interface QuestionSubmitService {
    JudgeResponseEnum questionSubmit(QuestionSubmitRequest questionSubmitRequest, HttpServletRequest request);
    QuestionSubmitPageVO listQuestionSubmit(Integer page,Integer pageSize);
    QuestionSubmit getById(long id);
}
