package com.demo.designmodel.abstractfactory;

/**
 * @author jiangyw
 * @date 2024/7/21 14:21
 * @description 抽象工厂接口
 */
public interface AbstractSoftwareFactory {
    OperatingSystem createOperatingSystem();
    Application createApplication();
}

/**
 * 具体工厂：windows工厂
 */
class WindowsFactory implements AbstractSoftwareFactory{

    @Override
    public OperatingSystem createOperatingSystem() {
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
    public OperatingSystem createOperatingSystem() {
        return new LinuxOS();
    }

    @Override
    public Application createApplication() {
        return new ExcelApplication();
    }
}