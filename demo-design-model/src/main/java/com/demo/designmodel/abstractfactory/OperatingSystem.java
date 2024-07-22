package com.demo.designmodel.abstractfactory;

/**
 * @author jiangyw
 * @date 2024/7/20 22:27
 * @description  抽象产品接口：操作系统
 */
public interface OperatingSystem {
    /**
     * 声明一组用于创建不同产品的抽象方法
     */
    void run();
}

/**
 * 具体的产品：windows操作系统
 */
class WindowsOS implements OperatingSystem{

    @Override
    public void run() {
        System.out.println("我是windowsOS");
    }
}
/**
 * 具体的产品：Linux操作系统
 */
class LinuxOS implements OperatingSystem{

    @Override
    public void run() {
        System.out.println("我是LinuxOS");
    }
}

