package com.demo.flink.learn.watermark;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.*;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 自定义水印生成器,断点式生成水印（即来一条数据就生成一次）
 */
public class WindowCustomWatermarkPunctuatedDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //方便演示，并行度设置为1
        env.setParallelism(1);
        env.getConfig().setAutoWatermarkInterval(2000);
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());

        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
                //指定Watermark的生成，乱序的，等待3s，断点式
                .<WaterSensor>forGenerator(ctx -> new MyWatermarkGeneratorPunctuatedGenerator(3000)) //lambda写法
                //指定时间戳分配器，从数据中提取
                .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> {
                    System.out.println("数据：" + element + ", recordTimestamp:" + recordTimestamp);
                    //返回的时间戳，毫秒
                    return element.getTs() * 1000;
                });

        SingleOutputStreamOperator<WaterSensor> sensorDsWithWatermark =
                sensorDs.assignTimestampsAndWatermarks(watermarkStrategy);

        KeyedStream<WaterSensor, String> sensorKs = sensorDsWithWatermark.keyBy(sensor -> sensor.getId());

        //水印是针对事件事件的，所以开事件时间窗口
        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
                sensorKs.window(TumblingEventTimeWindows.of(Time.seconds(10)));

        SingleOutputStreamOperator<String> process = sensorWs.process(new ProcessWindowFunction<WaterSensor, String,
                String, TimeWindow>() {
            /**
             * 全窗口函数计算逻辑： 窗口触发时才会调用一次，统一计算窗口中的所有数据
             * @param key 分组的key
             * @param context 上下文
             * @param elements
             * @param out 采集器
             * @throws Exception
             */
            @Override
            public void process(String key, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
                //上下文可以拿到window对象，还有侧输出流等
                long start = context.window().getStart();
                long end = context.window().getEnd();
                String startD = DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss.SSS");
                String endD = DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss.SSS");

                long count = elements.spliterator().estimateSize();
                out.collect("key=" + key + "[" + startD + "," + endD + ")" + "数目：" + count + "详细：" + elements.toString());
            }
        });

        process.print();
        env.execute();
    }

    //参考forBoundedOutOfOrderness底层的写法
   public static class MyWatermarkGeneratorPunctuatedGenerator<T> implements WatermarkGenerator<T>{

       private long maxTimestamp;

       private long delay;

       public MyWatermarkGeneratorPunctuatedGenerator(long delay) {
           this.delay = delay;
            this.maxTimestamp = Long.MIN_VALUE + this.delay + 1;
       }

       /**
        * 每条数据来，都会调用一次，用来提前最大的事件时间，保存下来
        * @param event
        * @param eventTimestamp 提取到的数据的事件时间
        * @param output
        */
       @Override
       public void onEvent(T event, long eventTimestamp, WatermarkOutput output) {
           maxTimestamp =  Math.max(maxTimestamp, eventTimestamp);
           output.emitWatermark(new Watermark(maxTimestamp - delay - 1));
           System.out.println("调用onEvent，当前最大时间戳" + maxTimestamp);
       }

       @Override
       public void onPeriodicEmit(WatermarkOutput output) {

       }
   }
}
