package com.demo.designmodel.abstractfactory;

/**
 * @author owen
 * @date 2024/7/21 15:07
 * @description // 在这个示例中，抽象工厂模式通过SoftwareFactory接口和其实现类来创建不同类型的操作系统和应用程序。
 * // 客户端代码可以根据需要选择不同的工厂实例来创建不同的产品组合。
 */
public class Client {
    public static void main(String[] args) {
        //客户端可以选择是用windows工厂还是Linux工厂来创建产品
        WindowsFactory windowsFactory = new WindowsFactory();
        OperatingOS windowsOS  = windowsFactory.createOperatingSystem();
        Application windowsApp  = windowsFactory.createApplication();
        windowsOS.run();
        windowsApp.open();

        LinuxFactory linuxFactory = new LinuxFactory();
        OperatingOS linuxOS = linuxFactory.createOperatingSystem();
        Application linuxApp = linuxFactory.createApplication();
        linuxOS.run();
        windowsApp.open();
    }
}
