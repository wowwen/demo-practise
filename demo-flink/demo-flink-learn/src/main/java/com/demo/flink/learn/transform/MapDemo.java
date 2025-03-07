package com.demo.flink.learn.transform;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/14 19:56
 * @description map转换算子演示
 */
public class MapDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("w1", 1L, 1),
                new WaterSensor("w2", 2L, 2),
                new WaterSensor("w3", 3L, 3)
        );

//        //第一个种方式：匿名实现类
//        SingleOutputStreamOperator<String> map = sensorDs.map(new MapFunction<WaterSensor, String>() {
//            @Override
//            public String map(WaterSensor value) throws Exception {
//                return value.getId();
//            }
//        });

//        //第二种方式
//        SingleOutputStreamOperator<String> map = sensorDs.map(sensor -> sensor.getId());

        //第三种方式
        SingleOutputStreamOperator<String> map = sensorDs.map(new WaterSensorMapFunction());

        map.print();

        env.execute();
    }

    public static class WaterSensorMapFunction implements MapFunction<WaterSensor, String>{

        @Override
        public String map(WaterSensor value) throws Exception {
            return value.getId();
        }
    }
}
