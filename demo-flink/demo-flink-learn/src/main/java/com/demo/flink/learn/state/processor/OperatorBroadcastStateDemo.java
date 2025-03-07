package com.demo.flink.learn.state.processor;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ReadOnlyBroadcastState;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.BroadcastConnectedStream;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/25 20:48
 * @description 算子状态的Broadcast State
 * 温度超过指定的阈值发送告警（阈值可以动态修改）
 */
public class OperatorBroadcastStateDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        //数据流
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888)
                .map(new WaterSensorFunctionImpl());
        //广播流，用来广播配置
        DataStreamSource<String> configDs = env.socketTextStream("192.168.56.141", 9999);

        MapStateDescriptor<String, Integer> descriptor = new MapStateDescriptor<>("broad-state",
                Types.STRING, Types.INT);
        //将配置流广播
        BroadcastStream<String> broadcast = configDs.broadcast(descriptor);
        //数据流连接广播流
        BroadcastConnectedStream<WaterSensor, String> connect = sensorDs.connect(broadcast);

        connect.process(new BroadcastProcessFunction<WaterSensor, String, String>() {
            /**
             * 处理数据流
             * @param value
             * @param ctx
             * @param out
             * @throws Exception
             */
            @Override
            public void processElement(WaterSensor value, ReadOnlyContext ctx, Collector<String> out) throws Exception {
                //5 通过上下文获取广播状态，取出值（只读）
                ReadOnlyBroadcastState<String, Integer> ctxBroadcastState = ctx.getBroadcastState(descriptor);
                Integer threshold = ctxBroadcastState.get("threshold");
                //第一条可能是数据流先来，此时无阈值
                threshold = threshold == null ? 0 : threshold;

                if (value.getTemperature() > threshold) {
                    out.collect("key=" + value.getId() + "温度超过：" + threshold);
                }

            }

            /**
             * 处理广播后的配置流的处理方法
             * @param value
             * @param ctx
             * @param out
             * @throws Exception
             */
            @Override
            public void processBroadcastElement(String value, Context ctx, Collector<String> out) throws Exception {
                //4 通过上下文获取广播状态，往状态里面写数据
                BroadcastState<String, Integer> broadcastState = ctx.getBroadcastState(descriptor);
                broadcastState.put("threshold", Integer.valueOf(value));

            }
        })
                .print();

    }


}

