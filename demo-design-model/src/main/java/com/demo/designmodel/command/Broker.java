package com.demo.designmodel.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author juven
 * @date 2025/5/27 22:08
 * @description 创建命令调用类
 */
public class Broker {
    private List<Order> orderList = new ArrayList<Order>();

    public void takeOrder(Order order){
        orderList.add(order);
    }

    public void placeOrders(){
        for (Order order : orderList) {
            order.execute();
        }
        orderList.clear();
    }
}
