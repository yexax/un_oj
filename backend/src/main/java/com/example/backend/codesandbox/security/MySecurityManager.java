package com.example.backend.codesandbox.security;

import com.example.backend.common.ErrorCode;
import com.example.backend.exception.BusinessException;

import java.security.Permission;

public class MySecurityManager extends SecurityManager{
    // 检测程序是否可执行文件
    @Override
    public void checkExec(String cmd) {
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

    // 检测程序是否允许读文件

    @Override
    public void checkRead(String file) {
        System.out.println(file);
        if (file.contains("C:\\Users\\w\\.jdks\\corretto-1.8.0_342")) {
            return;
        }
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

    // 检测程序是否允许写文件
    @Override
    public void checkWrite(String file) {
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

    // 检测程序是否允许删除文件
    @Override
    public void checkDelete(String file) {
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

    // 检测程序是否允许连接网络
    @Override
    public void checkConnect(String host, int port) {
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

}
