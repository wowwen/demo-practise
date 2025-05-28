package com.demo.designmodel.Interceptingfilter;

/**
 * @author juven
 * @date 2025/5/29 2:50
 * @description
 */
public interface Filter {
    public void execute(String request);
}

class AuthenticationFilter implements Filter {
    public void execute(String request){
        System.out.println("Authenticating request: " + request);
    }
}

class DebugFilter implements Filter {
    public void execute(String request){
        System.out.println("request log: " + request);
    }
}