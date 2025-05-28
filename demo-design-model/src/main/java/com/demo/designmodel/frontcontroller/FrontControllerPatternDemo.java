package com.demo.designmodel.frontcontroller;

/**
 * @author juven
 * @date 2025/5/29 2:47
 * @description
 */
public class FrontControllerPatternDemo {
    public static void main(String[] args) {
        FrontController frontController = new FrontController();
        frontController.dispatchRequest("HOME");
        frontController.dispatchRequest("STUDENT");
    }
}
