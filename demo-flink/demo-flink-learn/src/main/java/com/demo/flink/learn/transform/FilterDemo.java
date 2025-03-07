package com.demo.flink.learn.transform;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.FilterFunctionImplDemo;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/14 22:10
 * @description
 */
public class FilterDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("w1", 1L, 1),
                new WaterSensor("w2", 2L, 2),
                new WaterSensor("w3", 3L, 3)
        );

        //filter: true 保留 false 被过滤掉
        SingleOutputStreamOperator<WaterSensor> filter = sensorDs.filter(new FilterFunction<WaterSensor>() {
            @Override
            public boolean filter(WaterSensor value) throws Exception {
                return "w1".equals(value.getId());
            }
        });
        filter.print();
        /**
         * 打印出WaterSensor{id='w1', ts=1, temperature=1}
         */

        //采用用户自定义函数传参过滤
        SingleOutputStreamOperator<WaterSensor> w2 = sensorDs.filter(new FilterFunctionImplDemo("w2"));
        w2.print();
        /**
         * WaterSensor{id='w2', ts=2, temperature=2}
         */

        env.execute();
    }
}

