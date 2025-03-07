package com.demo.flink.learn.process;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 处理时间定时器
 */
public class KeyedProcessingTimerDemo {
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
//                Long currentTimestamp = ctx.timestamp();

                TimerService timerService = ctx.timerService();
                //当前watermark：注意，获取的水印是当前process的水印（记录的是上一条数据的水印，本次的还没有进入processElement方法）
                long wm = timerService.currentWatermark();
//                //注册定时器：事件时间
//                timerService.registerEventTimeTimer(5000);

                //当前处理时间：就是系统时间
                long currentProcessingTime = timerService.currentProcessingTime();

                System.out.println("key=" + currentKey + "，当前处理时间：" + currentProcessingTime + ",当前水印" + wm + "注册定时器");
                //其他API
                //注册定时器：处理时间
                timerService.registerProcessingTimeTimer(currentProcessingTime + 3000);
                //删除定时器：事件时间
//                timerService.deleteEventTimeTimer();
                //删除定时器：处理时间
//                timerService.deleteProcessingTimeTimer();

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
     * 又与按照key进行了分组，每个key都会注册一个定时器（相同key会去重），等到到了 当前时间 + 3s，则会将对应key的定时器都会触发（各个key注册的处理时间不同，会有先后）。
     */

    /**
     * 定时器总结
     * 1、keyedStream才有定时器
     * 2、事件时间定时器，通过watermark来触发
     *      watermark >= 注册时间
     *      注意： watermark = 当前最大事件时间 - 等待时间 -1ms，
     *      比如： 定时器为5s，等待3s， watermark =8s， 8s -3s -1ms = 4999ms， 不会触发5s的定时器。需要下一条的watermark大于8s + 1ms才会触发。
     * 3、在process中获取当前watermark，显示的是上一次的watermark。因为process一次只处理一条数据，水印watermark也是一条数据，process
     * 处理当前数据的时候，数据后面跟的水印还没有进来，没有更新currentWatermark.
     */
}
