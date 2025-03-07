package com.demo.flink.learn.aggregate;

import com.demo.flink.learn.bean.WaterSensor;
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
public class SimpleAggregateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

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
        //注意：传位置索引的，适用与Tuple类型，POJO类型不行
//        SingleOutputStreamOperator<WaterSensor> sum = keyBy.sum(1);

//        SingleOutputStreamOperator<WaterSensor> sum = keyBy.sum("ts");
//        sum.print();
        /**
         * 2> WaterSensor{id='w3', ts=3, temperature=3}
         * 1> WaterSensor{id='w1', ts=1, temperature=1}
         * 1> WaterSensor{id='w1', ts=12, temperature=1}
         * 1> WaterSensor{id='w2', ts=2, temperature=2}
         */

        /**
         * max/maxBy的区别
         *      max：只会取比较字段的最大值，非比较字段保留第一次的值
         *      maxBy；取比较字段的最大值，同时非比较字段取最大值的这条数据的值
         */
//        SingleOutputStreamOperator<WaterSensor> max = keyBy.max("ts");
//        max.print();
        /**
         * 2> WaterSensor{id='w3', ts=3, temperature=3}
         * 1> WaterSensor{id='w1', ts=1, temperature=1}
         * 1> WaterSensor{id='w1', ts=11, temperature=1} 注意这里的temperature
         * 1> WaterSensor{id='w2', ts=2, temperature=2}
         */

        SingleOutputStreamOperator<WaterSensor> maxBy = keyBy.maxBy("ts");
        maxBy.print();
        /**
         * 1> WaterSensor{id='w1', ts=1, temperature=1}
         * 2> WaterSensor{id='w3', ts=3, temperature=3}
         * 1> WaterSensor{id='w1', ts=11, temperature=21}
         * 1> WaterSensor{id='w2', ts=2, temperature=2}
         */

        env.execute();
    }
}
