package com.demo.flink.learn.state;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.contrib.streaming.state.EmbeddedRocksDBStateBackend;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 配置状态后端 statebackend
 */
public class StateBackendDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        /**
         * 指定状态后端，负责管理本地状态
         * hashmap
         *      存在TM的JVM的堆内存中，受TaskManager的内存限制
         * rocksdb
         *      存在TM节点的rocksdb数据库中，存在磁盘中， 写 要序列化  读 要反序列化
         *
         * 配置方式
         *      1、配置文件 默认hashmap  flink-conf.yml
         *      2 代码中指定
         *      3 提交参数指定
         *          flink run-application -t yarn-applicaiton -p 3
         *          -Dstate.backend.type=rocksdb
         *          -c 全类名 jar包
         */


        //1、hashmap状态后端
        HashMapStateBackend mapStateBackend = new HashMapStateBackend();
        env.setStateBackend(mapStateBackend);

        //2、RocksDB
//        EmbeddedRocksDBStateBackend dbStateBackend = new EmbeddedRocksDBStateBackend();
//        env.setStateBackend(dbStateBackend);

        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());

        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> element.getTs() * 1000);

        SingleOutputStreamOperator<WaterSensor> sensorDsWithWatermark =
                sensorDs.assignTimestampsAndWatermarks(watermarkStrategy);

        sensorDsWithWatermark.keyBy(WaterSensor::getId)
                .process(new KeyedProcessFunction<String, WaterSensor, String>() {
                    ValueState<Integer> lastValueState;

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        super.open(parameters);
                        lastValueState = getRuntimeContext().getState(new ValueStateDescriptor<Integer>("value-state"
                                , Types.INT));
                    }

                    @Override
                    public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {
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
