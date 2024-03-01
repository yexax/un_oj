package com.example.backend.codesandbox.impl.dockerSandbox;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;

public class JavaDockerSandbox extends DockerSandboxTemplate {
    public static final String imageName="adoptopenjdk:8-jre-hotspot";
    public JavaDockerSandbox(){
        super(imageName);
        super.codePath=super.parentPath+ File.separator+"Main.java";
    }

    @Override
    protected void saveShell() throws IOException, InterruptedException {
        String shellContent=
                "#! /bin/bash\n"
                        +"java -cp /app Main";
        String shellContent2=
                "#! /bin/bash\n" +
                        "/app/test.sh < /app/input.txt";
        //写一个shell文件
        FileUtil.writeString(shellContent,parentPath+ File.separator+"test.sh", "utf-8");
        Runtime.getRuntime().exec("chmod +x "+parentPath+ File.separator+"test.sh").waitFor();
        //第二个shell文件
        FileUtil.writeString(shellContent2,parentPath+ File.separator+"test2.sh", "utf-8");
        Runtime.getRuntime().exec("chmod +x "+parentPath+ File.separator+"test2.sh").waitFor();
    }

    @Override
    protected int compile() throws IOException, InterruptedException {
        return Runtime.getRuntime().exec("javac "+codePath).waitFor();
    }
}
