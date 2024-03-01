package com.example.backend.codesandbox.impl.nativeSandbox;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class JavaNativeSandbox extends NativeSandboxTemplate {
    @Override
    protected void saveCodeToFile(String code) {
        String userDir = System.getProperty("user.dir");
        String codeParentPath = userDir + File.separator + "tmp" + File.separator + UUID.randomUUID();
        super.setCodeParentPath(codeParentPath);
        String codePath = codeParentPath + File.separator + "Main.java";
        super.setCodePath(codePath);
        FileUtil.writeString(code,codePath,"utf-8");
    }

    @Override
    protected int compileCode() {
        String compileCmd = "javac " + super.getCodePath();
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
        String runCmd = "java -Xmx256m -cp " + getCodeParentPath() + " Main";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(runCmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return process;
    }
}
