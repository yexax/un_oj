package com.example.backend;
import com.example.backend.codesandbox.security.MySecurityManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.setSecurityManager(new MySecurityManager());
        System.setSecurityManager(new SecurityManager());
//        System.out.println(System.getSecurityManager());
        int a,b;
        Scanner in=new Scanner(System.in);
        a=in.nextInt();b=in.nextInt();
        System.out.println(a+b);
    }
}
