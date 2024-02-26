package com.example.backend.judge;

import com.example.backend.judge.model.JudgeResponseEnum;

/**
 * 判题服务
 */
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId
     */
    JudgeResponseEnum doJudge(Long questionSubmitId);
}
