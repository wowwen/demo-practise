package com.demo.designmodel.strategymodel.v1;

/**
 *
 * @FileName: PlatinumStrategy
 * @Author: owen
 * @Date: 2020-11-26 17:40
 * @Description: TODO
 */
public class PlatinumStrategyV1 implements StrategyV1 {
    @Override
    public double compute(long money) {
        System.out.println("白金会员 优惠50元，再打7折");
        return (money - 50) * 0.7;
    }
}
