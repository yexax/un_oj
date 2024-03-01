package com.example.backend;
import cn.hutool.core.io.FileUtil;
import com.example.backend.codesandbox.impl.nativeSandbox.utils.ProcessUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        String code="#include<iostream>\n" +
                "using namespace std;\n" +
                "int main()\n" +
                "{\n" +
                "    int a;\n" +
                "    cin>>a;\n" +
                "    cout<<a;\n" +
                "    return 0;\n" +
                "}";
        String codeParentPath=System.getProperty("user.dir")+ File.separator+"tmp";
        String codePath=codeParentPath+File.separator+"main.cpp";
        String compilePath=codeParentPath+File.separator+"main";
        //保存代码为文件
        FileUtil.writeString(code,codePath,"utf-8");
        //编译代码
        Runtime.getRuntime().exec("g++ "+codePath+" -o "+compilePath).waitFor();
        //执行代码
        Process runProcess = Runtime.getRuntime().exec(compilePath + ".exe");
        //程序输入
        String input="88888888";
        OutputStream runProcessOutputStream = runProcess.getOutputStream();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(runProcessOutputStream);
        outputStreamWriter.write(input+"\n");
        outputStreamWriter.flush();
        runProcessOutputStream.close();
        outputStreamWriter.close();
        runProcess.waitFor();
        //获取程序输出
        System.out.println(ProcessUtil.getOutputString(runProcess.getInputStream()));
    }
}
