package com.demo.designmodel.abstractfactory;

/**
 * @author owen
 * @date 2024/7/21 14:21
 * @description 抽象工厂接口
 */
public interface AbstractSoftwareFactory {
    OperatingOS createOperatingSystem();
    Application createApplication();
}

/**
 * 具体工厂：windows工厂
 */
class WindowsFactory implements AbstractSoftwareFactory{

    @Override
    public OperatingOS createOperatingSystem() {
        return new WindowsOS();
    }

    @Override
    public Application createApplication() {
        return new WordApplication();
    }
}

/**
 * 具体工厂：Linux工厂
 */
class LinuxFactory implements AbstractSoftwareFactory{

    @Override
    public OperatingOS createOperatingSystem() {
        return new LinuxOS();
    }

    @Override
    public Application createApplication() {
        return new ExcelApplication();
    }
}