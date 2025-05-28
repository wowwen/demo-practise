package com.demo.designmodel.compositeentity;

/**
 * @author juven
 * @date 2025/5/29 1:08
 * @description 使用 Client 来演示组合实体设计模式的用法
 */
public class CompositeEntityPatternDemo {
    public static void main(String[] args) {
        Client client = new Client();
        client.setData("Test", "Data");
        client.printData();
        client.setData("Second Test", "Data1");
        client.printData();
    }
}
