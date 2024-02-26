package com.example.backend.codesandbox.model;

import lombok.Data;

import java.util.List;
@Data
public class ExecuteCodeResponse {
    /**
     * 执行结果状态
     */
    private ExecuteStatusEnum status;
    /**
     * 执行产生的错误信息
     */
    private String errorMessage;
    /**
     * 程序输出
     */
    private List<String> outputList;
    /**
     * 消耗时间
     */
    private Long time;
    /**
     * 消耗内存
     */
    private Long memory;
}
