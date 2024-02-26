package com.example.backend.model.dto.question_submit;

import lombok.Data;


@Data
public class QuestionSubmitRequest {
    /**
     * 编程语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;
}
