package com.demo.flink.learn.transform;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/14 22:29
 * @description
 */
public class FlatMapDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("w1", 1L, 1),
                new WaterSensor("w2", 2L, 2),
                new WaterSensor("w3", 3L, 3)
        );

        //flatMap：一进多出
        SingleOutputStreamOperator<String> flatMap =
                sensorDs.flatMap(new FlatMapFunction<WaterSensor, String>() {
            @Override
            public void flatMap(WaterSensor value, Collector<String> out) throws Exception {
                if ("s1".equals(value.getId())) {
                    //一进一出
                    out.collect(value.getId());
                }
                if ("s2".equals(value.getId())) {
                    //一进二出，戳多少取决于调用了多少次采集器out
                    out.collect(value.getId());
                    out.collect(value.getTs().toString());
                }
            }
        });
        flatMap.print();

        env.execute();
    }
}
