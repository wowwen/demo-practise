package com.demo.flink.learn.watermark;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import com.demo.flink.learn.partition.MyDefinePartitioner;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description watermark空闲等待，解决部分数据迟迟不来，导致最小watermark一直不变，无法推进的问题
 */
public class WindowWatermarkIdlenessDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888)
                .map(new WaterSensorFunctionImpl())
                //新用自定义分区器决定数据的物理分区
                .partitionCustom(new MyDefinePartitioner.MyPartitioner(), value -> value.getTemperature());

        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
                //指定Watermark的生成，只是演示水印的传递，故用有序的
                .<WaterSensor>forMonotonousTimestamps()
                //指定时间戳分配器，从数据中提取
                .withTimestampAssigner((SerializableTimestampAssigner<WaterSensor>) (element, recordTimestamp) -> {
                    System.out.println("数据：" + element + ", recordTimestamp:" + recordTimestamp);
                    //返回的时间戳，毫秒
                    return element.getTs() * 1000;
                })
                //关键：保持空闲等待多久，超过了，最小的watermark就失效了，即watermark可以推进了。
                .withIdleness(Duration.ofSeconds(3));

        SingleOutputStreamOperator<WaterSensor> sensorDsWithWatermark =
                sensorDs.assignTimestampsAndWatermarks(watermarkStrategy);

        env.setParallelism(2);
        //在物理分区的基础上，每个分区内再用keyBy进行逻辑分组
        KeyedStream<WaterSensor, Long> sensorKs = sensorDsWithWatermark.keyBy(sensor -> {
            System.out.println("使用keyBy进行分区");
           return sensor.getTs();
        });

        //水印是针对事件事件的，所以开事件时间窗口
        WindowedStream<WaterSensor, Long, TimeWindow> sensorWs =
                sensorKs.window(TumblingEventTimeWindows.of(Time.seconds(10)));

        SingleOutputStreamOperator<String> process = sensorWs.process(new ProcessWindowFunction<WaterSensor, String,
                Long, TimeWindow>() {
            /**
             * 全窗口函数计算逻辑： 窗口触发时才会调用一次，统一计算窗口中的所有数据
             * @param key 分组的key
             * @param context 上下文
             * @param elements
             * @param out 采集器
             * @throws Exception
             */
            @Override
            public void process(Long key, Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
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

}
