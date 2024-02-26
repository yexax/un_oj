package com.example.backend.codesandbox.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ExecuteStatusEnum {
    SUCCESS("success"),
    RUNTIME_ERROR("runtime error"),
    COMPILE_ERROR("compile error"),
    TIMELIMITEXCEEDED("Time limit exceeded"),
    SYSTEM_ERROR("system error");
    private String value;
}
