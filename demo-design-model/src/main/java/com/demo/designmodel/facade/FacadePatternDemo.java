package com.demo.designmodel.facade;

/**
 * @author juven
 * @date 2025/5/27 0:58
 * @description 使用该外观类画出各种类型的形状
 */
public class FacadePatternDemo {
    public static void main(String[] args) {
        ShapeMaker shapeMaker = new ShapeMaker();

        shapeMaker.drawCircle();
        shapeMaker.drawRectangle();
        shapeMaker.drawSquare();
    }
}