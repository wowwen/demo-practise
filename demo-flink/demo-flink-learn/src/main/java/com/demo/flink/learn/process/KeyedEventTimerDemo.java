package com.demo.flink.learn.process;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 事件时间定时器
 */
public class KeyedEventTimerDemo {
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
        //方便演示，并行度设置为1
        env.setParallelism(1);

        KeyedStream<WaterSensor, String> sensorKs = sensorDsWithWatermark.keyBy(sensor -> sensor.getId());

        SingleOutputStreamOperator<String> process = sensorKs.process(new KeyedProcessFunction<String, WaterSensor,
                String>() {
            @Override
            public void processElement(WaterSensor value, Context ctx, Collector<String> out) throws Exception {

                String currentKey = ctx.getCurrentKey();
                //从数据中提取事件时间
                Long currentTimestamp = ctx.timestamp();

                TimerService timerService = ctx.timerService();
                //当前watermark
                long wm = timerService.currentWatermark();
                //注册定时器：事件时间
                timerService.registerEventTimeTimer(5000);
                System.out.println("key=" + currentKey + "，当前事件时间：" + currentTimestamp + ",当前水印" + wm + "注册定时器");
                //其他API
                //注册定时器：处理时间
//                timerService.registerProcessingTimeTimer(3000);
                //删除定时器：事件时间
//                timerService.deleteEventTimeTimer();
                //删除定时器：处理时间
//                timerService.deleteProcessingTimeTimer();
                //当前处理时间：就是系统时间
//                timerService.currentProcessingTime();
//                timerService.wait();
            }

            /**
             * 时间进展到定时器注册的时间，调用该方法
             * @param timestamp 当前时间进展
             * @param ctx
             * @param out
             * @throws Exception
             */
            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
                String currentKey = ctx.getCurrentKey();

                System.out.println("key=" + currentKey + ",当前时间：" + timestamp + "触发");
            }
        });

        process.print();
        env.execute();
    }

    /**
     * 又与按照key进行了分组，每个key都会注册一个定时器（相同key会去重），等到水印超过5s（等于5s不行），则会将每个key的定时器都会触发，与key无关，任何一个到了都会触发。
     */
}
