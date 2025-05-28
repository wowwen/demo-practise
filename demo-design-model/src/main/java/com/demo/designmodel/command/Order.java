package com.demo.designmodel.command;

/**
 * @author juven
 * @date 2025/5/27 21:46
 * @description
 */
public interface Order {
    void execute();
}

/**
 * 创建实现了 Order 接口的实体类
 */
class BuyStock implements Order {
    private Stock abcStock;

    public BuyStock(Stock abcStock){
        this.abcStock = abcStock;
    }

    @Override
    public void execute() {
        abcStock.buy();
    }
}

class SellStock implements Order {
    private Stock abcStock;

    public SellStock(Stock abcStock){
        this.abcStock = abcStock;
    }

    @Override
    public void execute() {
        abcStock.sell();
    }
}