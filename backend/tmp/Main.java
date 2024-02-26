import java.io.*;
import java.util.*;

public class Main
{
    public static void main(String args[]) throws Exception
    {
        Scanner cin=new Scanner(System.in);
        int a=cin.nextInt(),b=cin.nextInt();
        System.out.println(a/b);
        try {
            System.out.println("程序开始沉睡...");
            Thread.sleep(1000); // 沉睡1秒（1000毫秒）
            System.out.println("程序沉睡结束。");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}