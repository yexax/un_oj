package com.example.backend.codesandbox.impl.dockerSandbox;


import cn.hutool.core.io.FileUtil;
import com.example.backend.codesandbox.model.ExecuteCodeRequest;
import com.example.backend.codesandbox.model.ExecuteCodeResponse;
import com.example.backend.common.ErrorCode;
import com.example.backend.exception.BusinessException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.ExecCreateCmdResponse;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public abstract class DockerSandboxTemplate {
    private final String imageName;
    protected final String parentPath;
    protected String codePath;
    private static String inputPath;
    protected ExecuteCodeResponse response=new ExecuteCodeResponse();
    private String containerId;
    private final DockerClient dockerClient=DockerClientBuilder.getInstance().build();
    public DockerSandboxTemplate(String imageName){
        this.imageName=imageName;
        this.parentPath=System.getProperty("user.dir")+ File.separator+"tmp"+ UUID.randomUUID();
        this.inputPath=this.parentPath+File.separator+"input.txt";
    }
    private void createAndRunContainer(){
        //创建容器
        HostConfig hostConfig = new HostConfig();
        hostConfig.setBinds(new Bind(parentPath,new Volume("/app")));
        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withHostConfig(hostConfig)
                .withNetworkDisabled(true)
                .withReadonlyRootfs(true)
                .withAttachStdin(true)
                .withAttachStderr(true)
                .withAttachStdout(true)
                .withTty(true)
                .exec();
        containerId = container.getId();
        //启动容器
        dockerClient.startContainerCmd(containerId)
                .exec();
    }
    protected abstract void saveShell() throws IOException, InterruptedException;
    protected abstract int compile() throws IOException, InterruptedException;
    protected void runCodeAndSetResponse() throws InterruptedException {
        //执行命令
        ExecStartResultCallback execStartResultCallback=new ExecStartResultCallback(){
            @Override
            public void onNext(Frame frame) {
                StreamType streamType = frame.getStreamType();
                if (StreamType.STDERR.equals(streamType)) {
                    System.out.println("输出错误结果：" + new String(frame.getPayload()));
                } else {
                    System.out.println("输出结果：" + new String(frame.getPayload()));
                }
                super.onNext(frame);
            }

        };
        ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                .withCmd(new String[]{"./main.sh"})
                .withAttachStderr(true)
                .withAttachStdin(true)
                .withAttachStdout(true)
                .exec();

        dockerClient.execStartCmd(execCreateCmdResponse.getId())
                .exec(execStartResultCallback)
                .awaitCompletion();
    }
    final public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) throws IOException, InterruptedException {
        //创建并启动容器
        createAndRunContainer();
        //保存代码为文件
        String code = executeCodeRequest.getCode();
        FileUtil.writeString(code,codePath,"utf-8");
        //写shell文件
        saveShell();
        //编译代码
        if(compile()!=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        List<String> inputList = executeCodeRequest.getInputList();
        for(String input:inputList){
            //保存input为文件
            FileUtil.writeString(input,codePath,"utf-8");
            //运行代码
            runCodeAndSetResponse();
        }
        return response;
    }
}
