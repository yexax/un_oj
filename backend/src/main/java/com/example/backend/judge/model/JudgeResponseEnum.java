package com.example.backend.judge.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JudgeResponseEnum {
    ACCEPTED("Accepted"),
    WRONGANSWER("Wrong Answer"),
    COMPILEERROR("Compile Error"),
    RUNTIMEERROR("Runtime Error"),
    TIMELIMITEXCEEDED("Time Limit Exceeded");
    private String value;
}
