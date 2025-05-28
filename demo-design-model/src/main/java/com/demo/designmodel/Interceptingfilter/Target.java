package com.demo.designmodel.Interceptingfilter;

/**
 * @author juven
 * @date 2025/5/29 2:51
 * @description
 */
public class Target {
    public void execute(String request){
        System.out.println("Executing request: " + request);
    }
}
