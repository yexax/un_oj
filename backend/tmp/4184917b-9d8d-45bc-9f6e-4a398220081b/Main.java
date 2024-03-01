java -cp . -Djava.security.manager=MySecurityManager Mainimport java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int a,b;
        Scanner in=new Scanner(System.in);
        a=in.nextInt();b=in.nextInt();
        System.out.println(a+b);
    }
}