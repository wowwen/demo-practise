package com.demo.flink.learn.aggregate;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/15 20:51
 * @description 聚合操作必须在按键分区的数据流KeyedStream上操作，普通的Stream上是不行的
 */
public class ReduceDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("w1", 1L, 1),
                new WaterSensor("w1", 11L, 21),
                new WaterSensor("w2", 2L, 2),
                new WaterSensor("w3", 3L, 3)
        );

        KeyedStream<WaterSensor, String> keyBy = sensorDs.keyBy(new KeySelector<WaterSensor,
                String>() {
            @Override
            public String getKey(WaterSensor value) throws Exception {
                return value.getId();
            }
        });

        //reduce: keyBy之后两两聚合
        SingleOutputStreamOperator<WaterSensor> reduce = keyBy.reduce(new ReduceFunction<WaterSensor>() {
            @Override
            public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                System.out.println("value1:" + value1);
                System.out.println("value2:" + value2);
                //输入输出类型一致，所以需要返回一个POJO
                return new WaterSensor(value1.getId() + value2.getId(), value1.getTs(), value1.getTemperature());
            }
        });

        reduce.print();
        /**
         * WaterSensor{id='w1', ts=1, temperature=1}  每个key的第一条数据来的时候不会打印，聚合是两两聚合，需要等第二条数据来
         * value1:WaterSensor{id='w1', ts=1, temperature=1} 第二条数据来了，进入reduce方法，打印数据
         * value2:WaterSensor{id='w1', ts=11, temperature=21}
         * WaterSensor{id='w1w1', ts=1, temperature=1} 聚合
         * WaterSensor{id='w2', ts=2, temperature=2} w2的第一条数据来不会进入reduce，存起来。体现了“有状态”的概念
         * WaterSensor{id='w3', ts=3, temperature=3}
         */

        env.execute();
    }
}
