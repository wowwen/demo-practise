package com.demo.flink.learn.watermark;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 有序流设置水印
 */
public class WindowWatermarkDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //方便演示，并行度设置为1
        env.setParallelism(1);

        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());

        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
                //单调升序的watermark,没有等待时间
                .<WaterSensor>forMonotonousTimestamps()
                //指定时间戳分配器，从数据中提取
                .withTimestampAssigner(new SerializableTimestampAssigner<WaterSensor>() {
                    @Override
                    public long extractTimestamp(WaterSensor element, long recordTimestamp) {
                        System.out.println("数据：" + element + ", recordTimestamp:" + recordTimestamp);
                        //返回的时间戳，毫秒
                        return element.getTs() * 1000;
                    }
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

}
