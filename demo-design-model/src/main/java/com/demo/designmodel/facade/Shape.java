package com.demo.designmodel.facade;

/**
 * @author juven
 * @date 2025/5/27 0:56
 * @description
 */
public interface Shape {
    void draw();
}

/**
 * 创建实现接口的实体类
 */
class Rectangle implements Shape {

    @Override
    public void draw() {
        System.out.println("Rectangle::draw()");
    }
}

class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Square::draw()");
    }
}

class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Circle::draw()");
    }
}