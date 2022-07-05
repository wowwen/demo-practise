package com.example.demo.designmodel.strategymodel.v2;

import com.example.demo.designmodel.strategymodel.UserType;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: PlatinumStrategyV2
 * @Author: jiangyw8
 * @Date: 2020-11-27 20:12
 * @Description: TODO
 */
public class PlatinumStrategyV2 implements StrategyV2{
    @Override
    public double compute(long money) {
        System.out.println("白金会员 优惠50元，再打7折");
        return (money - 50) * 0.7;
    }

    // 添加 type 返回
    @Override
    public int getType() {
        return UserType.PLATINUM_VIP.getCode();
    }
}
