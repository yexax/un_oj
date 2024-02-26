package com.example.backend.controller;

import com.example.backend.common.BaseResponse;
import com.example.backend.common.ResultUtils;
import com.example.backend.judge.model.JudgeResponseEnum;
import com.example.backend.model.dto.question_submit.QuestionSubmitRequest;
import com.example.backend.model.vo.QuestionSubmitPageVO;
import com.example.backend.service.QuestionSubmitService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("question_submit")
public class QuestionSubmitController {
    @Resource
    private QuestionSubmitService questionSubmitService;
    @GetMapping("list/page")
    public BaseResponse<QuestionSubmitPageVO> listPageQuestionSubmit(Integer page, Integer pageSize){
        return ResultUtils.success(questionSubmitService.listQuestionSubmit(page,pageSize));
    }
    @PostMapping("/")
    public BaseResponse<String> questionSubmit(@RequestBody QuestionSubmitRequest questionSubmitRequest, HttpServletRequest request){
        JudgeResponseEnum judgeResponseEnum = questionSubmitService.questionSubmit(questionSubmitRequest, request);
        return ResultUtils.success(judgeResponseEnum.getValue());
    }
}
