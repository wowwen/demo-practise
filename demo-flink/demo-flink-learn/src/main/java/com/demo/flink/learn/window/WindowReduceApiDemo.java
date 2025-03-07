package com.demo.flink.learn.window;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description
 */
public class WindowReduceApiDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());
        //方便演示，并行度设置为1
        env.setParallelism(1);

        KeyedStream<WaterSensor, String> sensorKs = sensorDs.keyBy(sensor -> sensor.getId());

        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
                sensorKs.window(TumblingProcessingTimeWindows.of(Time.seconds(20)));

        //增量聚合,此处是根据id分区（keyBy）,所以只有相同id来的时候才会进去这个reduce方法
        SingleOutputStreamOperator<WaterSensor> reduce = sensorWs.reduce(new ReduceFunction<WaterSensor>() {
            @Override
            public WaterSensor reduce(WaterSensor value1, WaterSensor value2) throws Exception {
                System.out.println("调用reduce方法，value1:" + value1.toString() + " value2:" + value2.toString());
                return new WaterSensor(value1.getId(), value2.getTs(),
                        value1.getTemperature() + value2.getTemperature());
            }
        });
        reduce.print("汇总：");

        env.execute();
    }


}
