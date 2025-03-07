package com.demo.flink.learn.state.keyed;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
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
import java.util.Map;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description Map state：
 * 每个传感器的每个温度出现了多少次
 */
public class KeyedMapStateDemo {
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
                    //每个temperature出现了多少次
                    MapState<Integer, Integer> mapState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        mapState = getRuntimeContext().getMapState(new MapStateDescriptor<Integer, Integer>("map" +
                                "-state", Types.INT, Types.INT));
                        //api
//                        mapState.get();//对本组map状态，获取key的value
//                        mapState.contains();//对本组map状态，判断key是否存在
//                        mapState.put();//对本组map状态，添加一个键值对
//                        mapState.putAll(); //对本组map状态，添加多个键值对
//                        mapState.entries(); //对本组map状态，获取所有键值对
//                        mapState.iterator(); //对本组map状态，获取迭代器
//                        mapState.keys();   //对本组map状态，获取所有key
//                        mapState.values(); //对本组map状态，获取所有值
//                        mapState.remove(); //对本组的map状态，移除指定key的键值对
//                        mapState.isEmpty(); //对本组的map状态，判断是否为空
//                        mapState.clear(); //对本组的Map状态，清空
                    }

                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                        Integer temperature = value.getTemperature();
                        if (mapState.contains(temperature)){
                            Integer count = mapState.get(temperature);
                            mapState.put(temperature, ++ count);
                        }else{
                            mapState.put(temperature, 1);
                        }
                        //遍历 输出
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("key=" + value.getId() + "\n");
                        Iterable<Map.Entry<Integer, Integer>> entries = mapState.entries();
                        for (Map.Entry<Integer, Integer> entry : entries) {
                            stringBuilder.append(entry.toString() + "\n");
                        }
                        out.collect(stringBuilder.toString());
                    }
                })
                .print("主流");

        env.execute();
    }

}
