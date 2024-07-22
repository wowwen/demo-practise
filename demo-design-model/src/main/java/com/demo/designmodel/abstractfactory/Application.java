package com.demo.designmodel.abstractfactory;

/**
 * @author jiangyw
 * @date 2024/7/20 22:35
 * @description 抽象产品接口：应用程序
 */
public interface Application {
    void open();
}

/**
 * 具体产品：Word应用程序
 */
class WordApplication implements Application{

    @Override
    public void open() {
        System.out.println("Word应用");
    }
}
/**
 * 具体产品：Excel应用程序
 */
class ExcelApplication implements Application{

    @Override
    public void open() {
        System.out.println("Excel应用");
    }
}
