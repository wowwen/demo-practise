package com.demo.designmodel.prototype;

/**
 * @author juven
 * @date 2025/5/26 0:28
 * @description
 */
public class Circle extends Shape{

    public Circle(){
        type = "Circle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
