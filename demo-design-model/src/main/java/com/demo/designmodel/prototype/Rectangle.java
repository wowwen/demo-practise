package com.demo.designmodel.prototype;

/**
 * @author juven
 * @date 2025/5/26 0:26
 * @description 创建扩展了上面抽象类的实体类
 */
public class Rectangle extends Shape{
    public Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
