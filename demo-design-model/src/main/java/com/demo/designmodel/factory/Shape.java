package com.demo.designmodel.factory;

/**
 * @author jiangyw
 * @date 2024/7/19 18:59
 * @description 首先，定义一个图形接口类
 */
public interface Shape {
    /**
     * 画图形
     */
    void draw();
}
/**
 * 然后，我们要实现两个具体的图形的实现类，分别是 Circle（圆形）和 Rectangle（矩形）
 */
class Circle implements Shape{

    @Override
    public void draw() {
        System.out.println("圆形Circle");
    }
}

class Rectangle implements Shape{

    @Override
    public void draw() {
        System.out.println("矩形Retangle");
    }
}