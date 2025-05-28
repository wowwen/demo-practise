package com.demo.designmodel.prototype;

/**
 * @author juven
 * @date 2025/5/26 0:27
 * @description
 */
public class Square extends Shape {

    public Square(){
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}