package com.example.backend.controller;

import com.example.backend.common.BaseResponse;
import com.example.backend.common.ErrorCode;
import com.example.backend.common.ResultUtils;
import com.example.backend.exception.BusinessException;
import com.example.backend.model.dto.question.QuestionAddRequest;
import com.example.backend.model.dto.question.JudgeCase;
import com.example.backend.model.dto.question.JudgeConfig;
import com.example.backend.model.dto.question.QuestionUpdateRequest;
import com.example.backend.model.dto.user.UserRegisterRequest;
import com.example.backend.model.entities.Question;
import com.example.backend.model.vo.QuestionPageVO;
import com.example.backend.model.vo.UserVO;
import com.example.backend.service.QuestionService;
import com.example.backend.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("question")
public class QuestionController {
    private final static Gson GSON = new Gson();
    @Resource
    private UserService userService;
    @Resource
    private QuestionService questionService;
    @PostMapping("add")
    public BaseResponse<Boolean> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request){
        if(questionAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionAddRequest,question);
        List<String> tags = questionAddRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionAddRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionAddRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        UserVO loginUser = userService.getLoginUser(request);
        question.setUserId(loginUser.getId());
        questionService.addQuestion(question);
        return ResultUtils.success(true);
    }
    @GetMapping("get")
    public BaseResponse<Question> getQuestionById(long id){
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = questionService.getQuestionById(id);
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(question);
    }
    @PostMapping("delete")
    public BaseResponse<Boolean>DeleteQuestionById(long id){
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        questionService.deleteQuestion(id);
        return ResultUtils.success(true);
    }
    @PostMapping("update")
    public BaseResponse<Boolean> updateQuestionById(@RequestBody QuestionUpdateRequest questionUpdateRequest){
        if(questionUpdateRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateRequest,question);
        List<String> tags = questionUpdateRequest.getTags();
        if (tags != null) {
            question.setTags(GSON.toJson(tags));
        }
        List<JudgeCase> judgeCase = questionUpdateRequest.getJudgeCase();
        if (judgeCase != null) {
            question.setJudgeCase(GSON.toJson(judgeCase));
        }
        JudgeConfig judgeConfig = questionUpdateRequest.getJudgeConfig();
        if (judgeConfig != null) {
            question.setJudgeConfig(GSON.toJson(judgeConfig));
        }
        questionService.updateQuestion(question);
        return ResultUtils.success(true);
    }
    @GetMapping("list/page")
    public BaseResponse<QuestionPageVO> listQuestionByPage(Integer page, Integer pageSize){
        return ResultUtils.success(questionService.listQuestionByPage(page, pageSize));
    }
    @GetMapping("search")
    public BaseResponse<QuestionPageVO>searchQuestionByPage(Integer page,Integer pageSize,String title,String tags){
        return ResultUtils.success(questionService.searchQuestionByPage(page,pageSize,title,tags));
    }
}
