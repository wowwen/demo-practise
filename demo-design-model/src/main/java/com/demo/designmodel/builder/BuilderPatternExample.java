package com.demo.designmodel.builder;

/**
 * @author jiangyw
 * @date 2024/7/21 23:16
 * @description // 这个示例演示了如何使用建造者模式创建不同类型的房屋，每种房屋类型的建造过程都由相应的具体建造者类负责实现，而指导者类负责协调建造过程。
 */
public class BuilderPatternExample {
    public static void main(String[] args) {
        NormalHouseBuilder normalHouseBuilder = new NormalHouseBuilder();
        //通过指导者指导房屋的构建
        Director director = new Director(normalHouseBuilder);
        House normalHouse = director.constructHouse();
        //重写了toString方法
        System.out.println("普通住房" + normalHouse);

        StrongHouseBuilder strongHouseBuilder = new StrongHouseBuilder();
        Director director1 = new Director(strongHouseBuilder);
        House strongHouse = director1.constructHouse();
        System.out.println("豪华住房" + strongHouse);
    }
}
