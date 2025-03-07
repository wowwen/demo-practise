package com.demo.flink.learn.function;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;

/**
 * @author jiangyw
 * @date 2025/2/15 23:15
 * @description 用户自定义函数（UDF）
 */
public class FilterFunctionImplDemo implements FilterFunction<WaterSensor> {

    private String id;

    /**
     * 利用构造器传参，用传进来的参数做比较
     */
    public FilterFunctionImplDemo(String id) {
        this.id = id;
    }

    @Override
    public boolean filter(WaterSensor value) throws Exception {
        return this.id.equals(value.getId());
    }
}
