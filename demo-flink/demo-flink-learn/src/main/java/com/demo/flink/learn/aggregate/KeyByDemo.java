package com.demo.flink.learn.aggregate;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/15 20:51
 * @description
 */
public class KeyByDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("w1", 1L, 1),
                new WaterSensor("w1", 11L, 21),
                new WaterSensor("w2", 2L, 2),
                new WaterSensor("w3", 3L, 3)
        );

        //要点：
        //  1、返回的是一个KeyedStream键控流
        //  2.keyBy不是转换算子，只对数据进行重分区，不能设置并行度
        //  3.keyBy分组与分区的关系
        //      a. keyBy是对数据分组，保证相同key的数据在同一个分区
        //      b. 分区：一个子任务可以理解为一个分区，一个分区（子任务）中可以存在多个分组（key）
        KeyedStream<WaterSensor, String> keyBy = sensorDs.keyBy(new KeySelector<WaterSensor,
                String>() {
            @Override
            public String getKey(WaterSensor value) throws Exception {
                return value.getId();
            }
        });
        keyBy.print();

        env.execute();
    }
}
