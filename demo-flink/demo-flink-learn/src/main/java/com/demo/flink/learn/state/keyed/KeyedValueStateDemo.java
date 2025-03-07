package com.demo.flink.learn.state.keyed;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description value state值状态
 */
public class KeyedValueStateDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());

        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
                //指定Watermark的生成，乱序的，等待3s
                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                //指定时间戳分配器，从数据中提取
                .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> {
                    System.out.println("数据：" + element + ", recordTimestamp:" + recordTimestamp);
                    //返回的时间戳，毫秒
                    return element.getTs() * 1000;
                });

        SingleOutputStreamOperator<WaterSensor> sensorDsWithWatermark =
                sensorDs.assignTimestampsAndWatermarks(watermarkStrategy);

        sensorDsWithWatermark.keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, String>() {
                    //1 定义状态后端为值状态
                    ValueState<Integer> lastValueState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        //2 在open方法中，初始化值状态
                        //状态描述器  参数1 id 唯一不重复， 参数2 存储类型
                        lastValueState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("value-state"
                                , Types.INT));
                    }

                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                        //API
//                        lastValueState.value(); //获取值状态得数据
//                        lastValueState.update();//更新值状态得数据
//                        lastValueState.clear();//清除值状态得数据

                        int lastValue = lastValueState.value() == null ? 0 : lastValueState.value();
                        if (Math.abs(value.getTemperature() - lastValue) > 10){
                            out.collect("key:" + value.getId() + "温度变化过快");
                        }
                        lastValueState.update(value.getTemperature());
                    }
                })
                .print("主流");

        env.execute();
    }

}
