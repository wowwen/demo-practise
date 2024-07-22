package com.demo.designmodel.builder;

/**
 * @author jiangyw
 * @date 2024/7/21 22:54
 * @description // 最后，我们创建指导者类 Director，它协调建造过程并返回构建的房屋对象。
 */
public class Director {
    private AbstractHouseBuilder builder;

    public Director(AbstractHouseBuilder builder) {
        this.builder = builder;
    }

    public House constructHouse() {
        builder.buildFoundation();
        builder.buildStructure();
        builder.buildRoof();
        builder.buildInterior();
        return builder.getHouse();
    }


}