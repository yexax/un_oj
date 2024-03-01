package com.example.backend.codesandbox.impl.dockerSandbox;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.IOException;

public class CppDockerSandbox extends DockerSandboxTemplate{
    public CppDockerSandbox() {
        super("minimal-cpp-bash");
        super.codePath=super.parentPath+ File.separator+"main.cpp";
    }

    @Override
    protected void saveShell() throws IOException, InterruptedException {
        String shellContent=
                "#! /bin/bash\n"
                        +"g++ ./main.cpp -o main\n"
                        +"./main < input.txt";
        FileUtil.writeString(shellContent,parentPath+ File.separator+"main.sh", "utf-8");
        Runtime.getRuntime().exec("chmod +x "+parentPath+ File.separator+"main.sh").waitFor();
    }

    @Override
    protected int compile() throws IOException, InterruptedException {
        return 0;
    }
}
