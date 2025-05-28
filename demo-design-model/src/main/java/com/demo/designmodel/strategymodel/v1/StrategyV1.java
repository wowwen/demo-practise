package com.demo.designmodel.strategymodel.v1;

/**
 *
 * @FileName: Strategy
 * @Author: owen
 * @Date: 2020-11-26 17:13
 * @Description: 策略接口
 */
public interface StrategyV1 {
    // 计费方法
    double compute(long money);
}
