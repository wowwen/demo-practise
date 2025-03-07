package com.demo.flink.learn.function;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author jiangyw
 * @date 2025/2/16 21:32
 * @description 传感器转换类韩式
 */
public class WaterSensorFunctionImpl implements MapFunction<String, WaterSensor> {
    @Override
    public WaterSensor map(String value) throws Exception {
        String[] datas = value.split(",");
//                        Integer.valueOf()与 Integer.parseInt()的区别：
//                              valueOf()返回的是一个包装类型，默认是null；.parseInt()返回的是基本类型int，默认是0
        return new WaterSensor(datas[0], Long.valueOf(datas[1]), Integer.valueOf(datas[2]));
    }
}
