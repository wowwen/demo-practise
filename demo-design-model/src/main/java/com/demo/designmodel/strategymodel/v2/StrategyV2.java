package com.demo.designmodel.strategymodel.v2;

/**
 *
 * @FileName: StrategyV2
 * @Author: owen
 * @Date: 2020-11-27 11:53
 * @Description: 相较于V1版本，V2版本新增
 */
public interface StrategyV2 {
    double compute(long money);

    // 返回 type
    int getType();
}
