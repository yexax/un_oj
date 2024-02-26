package com.example.backend.codesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ExecuteCodeRequest {
    private List<String> inputList;
    private String language;
    private String code;
}
