package com.example.backend.service.impl;

import com.example.backend.common.ErrorCode;
import com.example.backend.exception.BusinessException;
import com.example.backend.judge.JudgeService;
import com.example.backend.judge.model.JudgeResponseEnum;
import com.example.backend.mapper.QuestionMapper;
import com.example.backend.mapper.QuestionSubmitMapper;
import com.example.backend.model.dto.question_submit.QuestionSubmitRequest;
import com.example.backend.model.entities.Question;
import com.example.backend.model.entities.QuestionSubmit;
import com.example.backend.model.vo.QuestionSubmitPageVO;
import com.example.backend.model.vo.UserVO;
import com.example.backend.service.QuestionSubmitService;
import com.example.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
@Slf4j
@Service
public class QuestionSubmitServiceImpl implements QuestionSubmitService {
    @Resource
    private QuestionSubmitMapper questionSubmitMapper;
    @Resource
    private UserService userService;
    @Resource
    private JudgeService judgeService;
    @Resource
    private QuestionMapper questionMapper;
    @Override
    public JudgeResponseEnum questionSubmit(QuestionSubmitRequest questionSubmitRequest, HttpServletRequest request) {
        if(questionSubmitRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long questionId = questionSubmitRequest.getQuestionId();
        if(questionId==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String code = questionSubmitRequest.getCode();
        String language = questionSubmitRequest.getLanguage();
        if(StringUtils.isAnyBlank(code,language)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitRequest,questionSubmit);
        UserVO loginUser = userService.getLoginUser(request);
        questionSubmit.setUserId(loginUser.getId());
        questionSubmitMapper.questionSubmit(questionSubmit);
        Long questionSubmitId = questionSubmit.getId();
        if(questionSubmitId==null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        JudgeResponseEnum judgeResponseEnum = judgeService.doJudge(questionSubmitId);
        String status = judgeResponseEnum.getValue();
        questionSubmit.setStatus(status);
        log.info(status);

        if(judgeResponseEnum.equals(JudgeResponseEnum.ACCEPTED)){
            questionSubmitMapper.updateStatus(questionSubmitId,status);
            questionMapper.statistics(questionId,1);
        }else{
            questionSubmitMapper.updateStatus(questionSubmitId,status);
            questionMapper.statistics(questionId,0);
        }
        return judgeResponseEnum;
    }

    @Override
    public QuestionSubmitPageVO listQuestionSubmit(Integer page, Integer pageSize) {
        List<QuestionSubmit> questionSubmitList = questionSubmitMapper.listQuestionSubmit((page - 1) * pageSize, pageSize);
        Integer count = questionSubmitMapper.getCount();
        return new QuestionSubmitPageVO(questionSubmitList,count);
    }

    @Override
    public QuestionSubmit getById(long id) {
        if(id<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionSubmit questionSubmit = questionSubmitMapper.getById(id);
        return questionSubmit;
    }
}
