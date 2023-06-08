package com.demo.designmodel.strategymodel.v1;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: OrdinaryStrategy
 * @Author: jiangyw8
 * @Date: 2020-11-26 17:41
 * @Description: TODO
 */
public class OrdinaryStrategyV1 implements StrategyV1 {
    @Override
    public double compute(long money) {
        System.out.println("普通会员 不打折");
        return money;
    }
}
