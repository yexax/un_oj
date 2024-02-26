package com.example.backend.service.impl;

import com.example.backend.common.ErrorCode;
import com.example.backend.exception.BusinessException;
import com.example.backend.mapper.QuestionMapper;
import com.example.backend.model.dto.question.QuestionUpdateRequest;
import com.example.backend.model.entities.Question;
import com.example.backend.model.vo.QuestionPageVO;
import com.example.backend.service.QuestionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Resource
    private QuestionMapper questionMapper;
    @Override
    public Boolean addQuestion(Question question) {
        boolean saveResult = questionMapper.addQuestion(question);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目添加失败");
        }
        return true;
    }

    @Override
    public Question getQuestionById(Long id) {
        return questionMapper.getQuestionById(id);
    }

    @Override
    public Boolean deleteQuestion(Long id) {
        return questionMapper.deleteQuestionById(id);
    }

    @Override
    public Boolean updateQuestion(Question question) {
        return questionMapper.updateQuestionById(question);
    }

    @Override
    public QuestionPageVO listQuestionByPage(Integer page, Integer pageSize) {
        if(page==null||pageSize==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QuestionPageVO questionPageVO = new QuestionPageVO();
        List<Question> questionList = questionMapper.listQuestionByPage((page - 1) * pageSize, pageSize);
        questionPageVO.setQuestionList(questionList);
        Integer total = questionMapper.getCount();
        questionPageVO.setTotal(total);
        return questionPageVO;
    }

    @Override
    public QuestionPageVO searchQuestionByPage(Integer page, Integer pageSize, String title, String tags) {
        if (page==null||pageSize==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if(title==null)title="";
        if(tags==null)tags="";
        List<Question> questionList = questionMapper.searchQuestionByPage((page - 1) * pageSize, pageSize, title, tags);
        Integer count = questionMapper.getCountBySearch(title, tags);
        QuestionPageVO questionPageVO = new QuestionPageVO();
        questionPageVO.setQuestionList(questionList);
        questionPageVO.setTotal(count);
        return questionPageVO;
    }
}
