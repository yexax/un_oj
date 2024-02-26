package com.example.backend.judge.impl;

import cn.hutool.json.JSONUtil;
import com.example.backend.codesandbox.model.ExecuteStatusEnum;
import com.example.backend.common.ErrorCode;
import com.example.backend.exception.BusinessException;
import com.example.backend.codesandbox.CodeSandbox;
import com.example.backend.codesandbox.impl.JavaNativeCodesandbox;
import com.example.backend.codesandbox.model.ExecuteCodeRequest;
import com.example.backend.codesandbox.model.ExecuteCodeResponse;
import com.example.backend.judge.JudgeService;
import com.example.backend.judge.model.JudgeResponseEnum;
import com.example.backend.mapper.QuestionMapper;
import com.example.backend.mapper.QuestionSubmitMapper;
import com.example.backend.model.dto.question.JudgeCase;
import com.example.backend.model.dto.question.JudgeConfig;
import com.example.backend.model.entities.Question;
import com.example.backend.model.entities.QuestionSubmit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionSubmitMapper questionSubmitMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Override
    public JudgeResponseEnum doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitMapper.getById(questionSubmitId);
        //数据校验
        if(questionSubmit==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Question question = questionMapper.getQuestionById(questionSubmit.getQuestionId());
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目不存在");
        }
        //执行代码
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(questionSubmit.getCode())
                .inputList(inputList)
                .language(questionSubmit.getLanguage())
                .build();
        CodeSandbox codeSandbox=new JavaNativeCodesandbox();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        //判断程序是否正常执行
        ExecuteStatusEnum executeStatus = executeCodeResponse.getStatus();
        if(!executeStatus.equals(ExecuteStatusEnum.SUCCESS)){
            if(executeStatus.equals(ExecuteStatusEnum.COMPILE_ERROR)){
                return JudgeResponseEnum.COMPILEERROR;
            }else if (executeStatus.equals(ExecuteStatusEnum.TIMELIMITEXCEEDED)){
                return JudgeResponseEnum.TIMELIMITEXCEEDED;
            }else {
                return JudgeResponseEnum.RUNTIMEERROR;
            }
        }
        //判断输出正确性
        List<String> outputList = executeCodeResponse.getOutputList();
        List<String> answerOutputList = judgeCaseList.stream().map(JudgeCase::getOutput).collect(Collectors.toList());
        if(outputList.size()!=answerOutputList.size()){
            return JudgeResponseEnum.WRONGANSWER;
        }
        for(int i=0;i<outputList.size();i++){
            if(!answerOutputList.get(i).equals(outputList.get(i))){
                return JudgeResponseEnum.WRONGANSWER;
            }
        }
        //判断时间复杂度
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        if(judgeConfig.getTimeLimit() < executeCodeResponse.getTime()){
            return JudgeResponseEnum.TIMELIMITEXCEEDED;
        }

        return JudgeResponseEnum.ACCEPTED;
    }
}
