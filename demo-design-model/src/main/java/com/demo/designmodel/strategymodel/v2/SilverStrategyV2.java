package com.demo.designmodel.strategymodel.v2;

import com.demo.designmodel.strategymodel.UserType;

/**
 *
 * @FileName: SilverStrategyV2
 * @Author: owen
 * @Date: 2020-11-27 14:52
 * @Description: TODO
 */
public class SilverStrategyV2 implements StrategyV2 {
    @Override
    public double compute(long money) {

        System.out.println("白银会员 优惠50元");
        return money - 50;
    }

    // type 返回
    @Override
    public int getType() {
        return UserType.SILVER_VIP.getCode();
    }
}
