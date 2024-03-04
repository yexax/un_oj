package com.example.backend.codesandbox.impl.nativeSandbox;

import cn.hutool.core.io.FileUtil;
import com.example.backend.codesandbox.impl.nativeSandbox.utils.ProcessUtil;
import com.example.backend.codesandbox.model.ExecuteCodeRequest;
import com.example.backend.codesandbox.model.ExecuteCodeResponse;
import com.example.backend.codesandbox.model.ExecuteStatusEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public abstract class NativeSandboxTemplate {
    private String codeParentPath;
    private String codePath;
    private ExecuteCodeResponse response=new ExecuteCodeResponse();
    protected static final Long runTimeout=5000L;
    protected static final String maxMemory="256m";

    /**
     * 保存代码为文件
     * @param code
     */
    protected abstract void saveCodeToFile(String code);

    /**
     * 编译代码
     * @return 返回0表示编译成功
     */
    protected abstract int compileCode();

    /**
     * 运行代码
     * @return
     */
    protected abstract Process runCode();
    final public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest){
        String code = executeCodeRequest.getCode();
        List<String> inputList = executeCodeRequest.getInputList();
        //保存代码为文件
        saveCodeToFile(code);
        log.info("代码保存成功");
        //编译代码
        int compileExitValue = compileCode();
            //编译失败
        if(compileExitValue!=0){
            log.info("编译失败");
            response.setStatus(ExecuteStatusEnum.COMPILE_ERROR);
            return response;
        }
        log.info("编译成功");
        long maxTime=0;//所有用例运行时间最大值
        List<String> outputList= new ArrayList<>();
        try {
            //一组用例运行一次代码
            for(String input:inputList){
                StopWatch stopWatch = new StopWatch();//用来计算程序执行时间
                stopWatch.start();
                Process runProcess = runCode();
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
                log.info("程序输入");
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
                    log.error("运行错误");
                    log.info(runtime+"");
                    if(runtime>=runTimeout){
                        log.error("运行超时");
                        response.setStatus(ExecuteStatusEnum.TIMELIMITEXCEEDED);
                        return response;
                    }
                    // 异常退出
                    log.error("运行失败"+runExitValue);
                    // 分批获取进程的错误输出
                    BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                    // 逐行读取
                    StringBuilder errorOutput=new StringBuilder();
                    // 逐行读取
                    String errorCompileOutputLine;
                    while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                        errorOutput.append(errorCompileOutputLine);
                    }
                    log.error("运行错误信息："+errorOutput);
                    response.setStatus(ExecuteStatusEnum.RUNTIME_ERROR);
                    String RuntimeErrorMessage = ProcessUtil.getOutputString(runProcess.getErrorStream());
                    response.setErrorMessage(RuntimeErrorMessage);
                    return response;
                }
                //将一组运行结果保存
                outputList.add(ProcessUtil.getOutputString(runProcess.getInputStream()));
            }
        }catch (Exception e){
            throw new RuntimeException();
        }finally {
            FileUtil.del(codeParentPath);
        }
        //设置返回对象
        response.setStatus(ExecuteStatusEnum.SUCCESS);
        response.setOutputList(outputList);
        //response.setMemory();
        response.setTime(maxTime);
        return response;
    }
}
