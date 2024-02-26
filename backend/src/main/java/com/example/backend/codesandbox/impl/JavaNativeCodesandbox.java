package com.example.backend.codesandbox.impl;

import cn.hutool.core.io.FileUtil;
import com.example.backend.codesandbox.model.ExecuteCodeRequest;
import com.example.backend.codesandbox.model.ExecuteCodeResponse;
import com.example.backend.codesandbox.CodeSandbox;
import com.example.backend.codesandbox.model.ExecuteStatusEnum;
import com.example.backend.codesandbox.impl.utils.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
@Slf4j
public class JavaNativeCodesandbox implements CodeSandbox {
    //防止程序无限执行的超时时间
    public static final Long runTimeout=5000L;
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();
        List<String> inputList = executeCodeRequest.getInputList();

        //返回对象
        ExecuteCodeResponse response = new ExecuteCodeResponse();

        //1.保存代码为文件
        String userDir = System.getProperty("user.dir");
        String fileParentPath = userDir + File.separator + "tmp" + File.separator + UUID.randomUUID();
        File file = FileUtil.writeString(code, fileParentPath + File.separator + "Main.java", "utf-8");
        try {
            //2.编译代码并等待编译完成
            Process compileProcess = Runtime.getRuntime().exec("javac " + file.getAbsolutePath());
            int compileExitValue = compileProcess.waitFor();
            //编译失败
            if(compileExitValue!=0){
                //获取编译错误信息
                response.setStatus(ExecuteStatusEnum.COMPILE_ERROR);
                InputStream compileProcessErrorStream = compileProcess.getErrorStream();
                String compileErrorMessage = ProcessUtil.getOutputString(compileProcessErrorStream);
                compileProcessErrorStream.close();
                response.setErrorMessage(compileErrorMessage);
                return response;
            }
            compileProcess.destroy();

            //输入数据
            List<String> outputList=new ArrayList<>();
            long maxTime=0;
            for(String input:inputList){
                //3.执行代码
                StopWatch stopWatch = new StopWatch();//用来计算程序执行时间
                stopWatch.start();
                Process runProcess = Runtime.getRuntime().exec("java -Xmx256m -cp " + fileParentPath + " " + "Main");
                //创建一个守护线程，时间到了就去把runProcess销毁
                new Thread(()->{
                    try {
                        Thread.sleep(runTimeout);
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                //程序输入
                OutputStream runProcessOutputStream = runProcess.getOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(runProcessOutputStream);
                outputStreamWriter.write(input+"\n");
                outputStreamWriter.flush();
                runProcessOutputStream.close();
                outputStreamWriter.close();
                //等待一组数据执行完成
                int runExitValue = runProcess.waitFor();
                //获取程序执行时间
                stopWatch.stop();
                long runtime = stopWatch.getLastTaskTimeMillis();
                maxTime=Math.max(maxTime,runtime);

                //运行错误
                if(runExitValue!=0){
                    log.info(runtime+"");
                    if(runtime>=runTimeout){
                        response.setStatus(ExecuteStatusEnum.TIMELIMITEXCEEDED);
                        return response;
                    }
                    response.setStatus(ExecuteStatusEnum.RUNTIME_ERROR);
                    String RuntimeErrorMessage = ProcessUtil.getOutputString(runProcess.getErrorStream());
                    response.setErrorMessage(RuntimeErrorMessage);
                    return response;
                }
                //将一组运行结果保存
                outputList.add(ProcessUtil.getOutputString(runProcess.getInputStream()));
            }
            //设置返回对象
            response.setStatus(ExecuteStatusEnum.SUCCESS);
            response.setOutputList(outputList);
//            response.setMemory();
            response.setTime(maxTime);
        } catch (Exception e) {
            response.setStatus(ExecuteStatusEnum.SYSTEM_ERROR);
            return response;
        }finally {
            //4.执行完删除文件
            FileUtil.del(fileParentPath);
        }
        return response;
    }

    public static void main(String[] args) {
        JavaNativeCodesandbox javaNativeCodesandbox = new JavaNativeCodesandbox();
        List<String> inputList = Arrays.asList("100 2\n","1 2\n","2000000000 9\n");
        ExecuteCodeResponse response = javaNativeCodesandbox.executeCode(new ExecuteCodeRequest(inputList, null, "import java.io.*;\n" +
                "import java.util.*;\n" +
                "\n" +
                "public class Main\n" +
                "{\n" +
                "    public static void main(String args[]) throws Exception\n" +
                "    {\n" +
                "        Scanner cin=new Scanner(System.in);\n" +
                "        int a=cin.nextInt(),b=cin.nextInt();\n" +
                "        System.out.println(a/b);\n" +
                "    }\n" +
                "}"));

        System.out.println(response);
    }
}
