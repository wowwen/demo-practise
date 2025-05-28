package com.demo.designmodel.strategymodel.v2;

import com.demo.designmodel.strategymodel.UserType;

/**
 *
 * @FileName: OrdinaryStrategyV2
 * @Author: owen
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
