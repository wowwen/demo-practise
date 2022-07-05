package com.example.demo.designmodel.strategymodel.v2;

import com.example.demo.designmodel.strategymodel.UserType;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: GoldStrategyV2
 * @Author: jiangyw8
 * @Date: 2020-11-27 20:11
 * @Description: TODO
 */
public class GoldStrategyV2 implements StrategyV2{
    @Override
    public double compute(long money) {
        System.out.println("黄金会员 8折");
        return money * 0.8;
    }
    // type 返回
    @Override
    public int getType() {
        return UserType.GOLD_VIP.getCode();
    }
}
