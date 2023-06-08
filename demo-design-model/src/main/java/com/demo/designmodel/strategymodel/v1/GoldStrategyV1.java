package com.demo.designmodel.strategymodel.v1;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: GoldStrategy
 * @Author: jiangyw8
 * @Date: 2020-11-26 17:39
 * @Description: TODO
 */
public class GoldStrategyV1 implements StrategyV1 {
    @Override
    public double compute(long money) {
        System.out.println("黄金会员 8折");
        return money * 0.8;
    }
}
