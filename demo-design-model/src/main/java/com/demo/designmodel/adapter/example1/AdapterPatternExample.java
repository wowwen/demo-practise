package com.demo.designmodel.adapter.example1;

/**
 * @author owen
 * @date 2024/7/22 12:03
 * @description // 在这个示例中，LegacyRectangle是已经存在的类，而RectangleAdapter是适配器类，用于将LegacyRectangle适配到Rectangle接口上。
 * // 客户端代码通过使用适配器来画一个矩形，实际上是在调用了LegacyRectangle的display方法，但是通过适配器，它符合了Rectangle接口的标准。
 */
public class AdapterPatternExample {
    public static void main(String[] args) {
        LegacyRectangle legacyRectangle = new LegacyRectangle();
        //RectangleAdapter实现了Rectangle接口，通过构造函数将legacyRectangle适配到了Rectangle接口上
        RectangleAdapter rectangleAdapter = new RectangleAdapter(legacyRectangle);
        //draw方法种引用了legacyRectangle的display方法，实际上还是通过旧的LegacyRectangle类种的display方法来画矩形
        rectangleAdapter.draw(10, 20, 30 , 40);
    }
}
