package com.example.backend.codesandbox;

import com.example.backend.codesandbox.model.ExecuteCodeRequest;
import com.example.backend.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
