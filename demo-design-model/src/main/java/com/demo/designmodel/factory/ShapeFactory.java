package com.demo.designmodel.factory;

/**
 * @author jiangyw
 * @date 2024/7/19 23:43
 * @description // 接下来，我们创建一个抽象工厂类 ShapeFactory
 *              // 它定义了一个抽象的工厂方法 createShape，子类将实现这个方法来创建具体的图形对象
 */
public abstract class ShapeFactory {
    abstract Shape createShape();
}

// 然后，我们创建两个具体的工厂类，分别是 CircleFactory 和 RectangleFactory
// 它们分别实现了 ShapeFactory 并重写了 createShape 方法来返回相应的图形对象
class CircleFactory extends ShapeFactory {

    @Override
    Shape createShape() {
        return new Circle();
    }
}

class RectangleFactory extends ShapeFactory{

    @Override
    Shape createShape() {
        return new Rectangle();
    }
}
