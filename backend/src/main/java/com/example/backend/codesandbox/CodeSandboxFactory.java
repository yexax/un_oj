package com.example.backend.codesandbox;

import com.example.backend.codesandbox.impl.remoteSandbox.RemoteCodesandbox;
import com.example.backend.codesandbox.impl.dockerSandbox.DockerCodesandbox;
import com.example.backend.codesandbox.impl.nativeSandbox.NativeCodeSandbox;


public class CodeSandboxFactory {

    public static CodeSandbox newInstance(String type){
        switch (type){
            case "docker":
                return new DockerCodesandbox();
            case "remote":
                return new RemoteCodesandbox();
            default:
                return new NativeCodeSandbox();
        }
    }
}
