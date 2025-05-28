package com.demo.designmodel.builder;

/**
 * @author owen
 * @date 2024/7/21 21:10
 * @description // 然后，我们创建一个抽象建造者类 HouseBuilder，它定义了构建房屋的方法。
 */
public abstract class AbstractHouseBuilder {
    protected House house = new House();

    public abstract void buildFoundation();
    public abstract void buildStructure();
    public abstract void buildRoof();
    public abstract void buildInterior();

    public House getHouse() {
        return house;
    }
}

/**
 * // 接下来，我们创建两个具体的建造者类 ConcreteHouseBuilder 和 LuxuryHouseBuilder
 * // 分别实现了不同类型房屋的构建过程。
 * // 具体建造者类 - 普通房屋
 */
class NormalHouseBuilder extends AbstractHouseBuilder{

    @Override
    public void buildFoundation() {
        //house属性取自继承的抽象类
        house.setFoundation("Normal Foundation");
    }

    @Override
    public void buildStructure() {
        house.setStructure("Normal Structure");
    }

    @Override
    public void buildRoof() {
        house.setRoof("Normal Roof");
    }

    @Override
    public void buildInterior() {
        house.setInterior("Normal Interior");
    }
}

/**
 * // 具体建造者类 - 豪华房屋
 */
class StrongHouseBuilder extends AbstractHouseBuilder{

    @Override
    public void buildFoundation() {
        house.setFoundation("Strong Foundation");
    }

    @Override
    public void buildStructure() {
        house.setStructure("Strong Structure");
    }

    @Override
    public void buildRoof() {
        house.setRoof("Strong Roof");
    }

    @Override
    public void buildInterior() {
        house.setInterior("Strong Interior");
    }
}
