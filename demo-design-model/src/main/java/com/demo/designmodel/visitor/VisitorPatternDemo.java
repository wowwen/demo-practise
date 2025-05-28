package com.demo.designmodel.visitor;

/**
 * @author juven
 * @date 2025/5/28 21:05
 * @description 使用 ComputerPartDisplayVisitor 来显示 Computer 的组成部分
 */
public class VisitorPatternDemo {
    public static void main(String[] args) {

        ComputerPart computer = new Computer();
        computer.accept(new ComputerPartDisplayVisitor());
    }
}

/**
 * 1、 创建一个定义接受操作的ComputerPart接口；
 * 2、 定义类Keyboard、Mouse、Monitor和Computer实现ComputerPart接口；
 * 3、 定义另一个接口ComputerPartVisitor，它定义了访问者类的操作；
 * 4、 定义类Computer使用实体访问者来执行相应的动作；
 * 5、 定义类VisitorPatternDemo使用Computer、ComputerPartVisitor类来演示访问者模式的用法；
 */
