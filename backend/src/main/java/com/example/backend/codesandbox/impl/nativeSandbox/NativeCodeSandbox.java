package com.example.backend.codesandbox.impl.nativeSandbox;

import com.example.backend.codesandbox.CodeSandbox;
import com.example.backend.codesandbox.model.ExecuteCodeRequest;
import com.example.backend.codesandbox.model.ExecuteCodeResponse;
import com.example.backend.codesandbox.impl.nativeSandbox.CppNativeSandbox;
import com.example.backend.codesandbox.impl.nativeSandbox.JavaNativeSandbox;

/**
 * 使用Java原生代码实现的沙箱
 */
public class NativeCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String language = executeCodeRequest.getLanguage();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        if("java".equals(language)){
            JavaNativeSandbox javaNativeSandbox = new JavaNativeSandbox();
            executeCodeResponse=javaNativeSandbox.executeCode(executeCodeRequest);
        } else if ("cpp".equals(language)) {
            CppNativeSandbox cppNativeSandbox = new CppNativeSandbox();
            executeCodeResponse=cppNativeSandbox.executeCode(executeCodeRequest);
        }
        return executeCodeResponse;
    }
}
