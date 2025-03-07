package com.demo.flink.learn.state.keyed;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description List state
 */
public class KeyedListStateDemo {
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
                    //1 定义状态后端为列表状态
                    ListState<WaterSensor> lastListState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        //2 在open方法中，初始化值状态
                        //状态描述器  参数1 id 唯一不重复， 参数2 存储类型
                        lastListState = getRuntimeContext().getListState(new ListStateDescriptor<WaterSensor>("list" +
                                "-state", Types.POJO(WaterSensor.class)));
                    }

                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                        //API
//                        lastListState.addAll(); //本组数据（keyBy之后）  添加多个元素,以列表values形式传入
//                        lastListState.add(); //本组数据（keyBy之后）  向列表中添加一个元素
//                        lastListState.update();//本组数据（keyBy之后）  传入一个列表values，直接对状态进行覆盖
//                        lastListState.get();//本组数据（keyBy之后）  获取当前得列表状态，返回的是一个可迭代类型Iterable<T>
//                        lastListState.clear();//本组数据（keyBy之后） 清空list状态

                        //1 来一条，先存到list中，再去排序清除
                        lastListState.add(value);
                        //2 然后拿出list中的数据 比较 排序 清除
                        Iterable<WaterSensor> sensorIterable = lastListState.get();
                        List<WaterSensor> waterSensors = new ArrayList<>();
                        for (Iterator<WaterSensor> iterator = sensorIterable.iterator(); iterator.hasNext(); ) {
                            WaterSensor next = iterator.next();
                            waterSensors.add(next);
                        }
                        waterSensors.sort((o1, o2) -> o2.getTemperature() -o1.getTemperature());
                        if (waterSensors.size() > 3){
                            //只保留3条，进来的多的一条移除
                            waterSensors.remove(3);
                        }
                        out.collect("key:" + value.getId() + "==TOP:" + waterSensors);

                        //3 更新
                        lastListState.update(waterSensors);
                    }
                })
                .print("主流");

        env.execute();
    }

}
