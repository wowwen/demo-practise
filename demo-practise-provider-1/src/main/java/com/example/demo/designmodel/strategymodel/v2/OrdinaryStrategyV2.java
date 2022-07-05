package com.example.demo.designmodel.strategymodel.v2;

import com.example.demo.designmodel.strategymodel.UserType;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: OrdinaryStrategyV2
 * @Author: jiangyw8
 * @Date: 2020-11-27 14:49
 * @Description: TODO
 */
public class OrdinaryStrategyV2 implements StrategyV2{
    @Override
    public double compute(long money) {
        System.out.println("普通会员 不打折");
        return money;
    }

    // 添加 type 返回
    @Override
    public int getType() {
        return UserType.SILVER_VIP.getCode();
    }
}
