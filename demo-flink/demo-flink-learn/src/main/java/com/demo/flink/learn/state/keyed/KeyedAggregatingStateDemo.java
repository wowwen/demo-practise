package com.demo.flink.learn.state.keyed;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.state.AggregatingState;
import org.apache.flink.api.common.state.AggregatingStateDescriptor;
import org.apache.flink.api.common.state.ReducingState;
import org.apache.flink.api.common.state.ReducingStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description AggregatingState
 * 每个传感器的平均水温
 */
public class KeyedAggregatingStateDemo {
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

                    AggregatingState<Integer, Double> aggregatingState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        getRuntimeContext().getAggregatingState(new AggregatingStateDescriptor<Integer,
                                Tuple2<Integer, Integer>,
                                Double>("agg-state",
                                new AggregateFunction<Integer, Tuple2<Integer, Integer>, Double>() {
                                    @Override
                                    public Tuple2<Integer, Integer> createAccumulator() {
                                        return Tuple2.of(0, 0);
                                    }

                                    @Override
                                    public Tuple2<Integer, Integer> add(Integer value,
                                                                        Tuple2<Integer, Integer> accumulator) {
                                        //添加一个数据进来，则将数据的temperature加到总温度上，同时数据的count + 1
                                        return Tuple2.of(accumulator.f0 + value, accumulator.f1 + 1);
                                    }

                                    @Override
                                    public Double getResult(Tuple2<Integer, Integer> accumulator) {
                                        //求平均温度
                                        return accumulator.f0 * 1D / (accumulator.f1 == 0 ? 1 : accumulator.f1);
                                    }

                                    @Override
                                    public Tuple2<Integer, Integer> merge(Tuple2<Integer, Integer> a, Tuple2<Integer,
                                            Integer> b) {
                                        return null;
                                    }
                                }, Types.TUPLE(Types.INT, Types.INT)));
                    }

                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
                        //将数据传入聚合状态
                        aggregatingState.add(value.getTemperature());
                        //从聚合状态中取平均数
                        Double result = aggregatingState.get();

                        out.collect("key= "+ value.getId() + ",平均水温：" + result);

                        //api
//                        aggregatingState.add(); //向本组的聚合状态添加数据，会自动进行聚合
//                        aggregatingState.get(); //向本组的聚合状态获取结果
//                        aggregatingState.clear(); //对本组的聚合状态 清空数据
                    }
                })
                .print("主流");

        env.execute();
    }

}
