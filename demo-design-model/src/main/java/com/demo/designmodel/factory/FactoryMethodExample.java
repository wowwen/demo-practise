package com.demo.designmodel.factory;

/**
 * @author owen
 * @date 2024/7/20 22:13
 * @description
 */
public class FactoryMethodExample {
    public static void main(String[] args) {
        CircleFactory circleFactory = new CircleFactory();
        Shape circle = circleFactory.createShape();
        circle.draw();

        RectangleFactory rectangleFactory = new RectangleFactory();
        Shape rectangle = rectangleFactory.createShape();
        rectangle.draw();
    }
}
