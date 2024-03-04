package com.example.backend.codesandbox.impl.nativeSandbox;

import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
@Slf4j
public class CppNativeSandbox extends NativeSandboxTemplate {
    @Override
    protected void saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String codeParentPath = userDir + File.separator + "tmp" + File.separator + UUID.randomUUID();
        super.setCodeParentPath(codeParentPath);
        String codePath = codeParentPath + File.separator + "main.cpp";
        super.setCodePath(codePath);
        FileUtil.writeString(code,codePath,"utf-8");
    }

    @Override
    protected int compileCode() {
        String compileCmd = "g++ " + super.getCodePath()+" -o "+getCodeParentPath()+File.separator+"main";
        Process compileProcess = null;
        try {
            //编译
            compileProcess = Runtime.getRuntime().exec(compileCmd);
            //等待编译完成
            int compileExitValue = compileProcess.waitFor();
            compileProcess.destroy();
            return compileExitValue;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected Process runCode() {
        String runCmd = getCodeParentPath()+ File.separator+"main";
        log.info("准备执行命令"+runCmd);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(runCmd);
            log.info("执行完成");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return process;
    }
}
