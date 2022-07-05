package com.example.demo.designmodel.strategymodel.v1;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: SilverStrategy
 * @Author: jiangyw8
 * @Date: 2020-11-26 17:31
 * @Description: TODO
 */
public class SilverStrategyV1 implements StrategyV1 {
    @Override
    public double compute(long money) {

        System.out.println("白银会员 优惠50元");
        return money - 50;
    }
}
