package com.example.demo.designmodel.strategymodel.v1;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: Strategy
 * @Author: jiangyw8
 * @Date: 2020-11-26 17:13
 * @Description: 策略接口
 */
public interface StrategyV1 {
    // 计费方法
    double compute(long money);
}
