package com.demo.flink.learn.watermark;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 窗口延迟关闭，即计算完成后，输出计算结果，但是不立即关闭窗口，以待迟到数据进行增量计算(来一条计算一次)，但是watermark不变，还是计算结果时候的值（本例中为10）
 * 只有等到时间戳处理完后的值为窗口watermark + 延迟关窗时间时， 才关闭（例如本例中为15s）.
 * 何为乱序：数据的顺序乱了，出现了时间小的比时间大的晚来
 * 何为迟到：数据的时间戳 < 当前的watermark
 */
public class WindowWatermarkSideOutputDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //方便演示，并行度设置为1
        env.setParallelism(1);
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888)
                .map(new WaterSensorFunctionImpl());

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

        KeyedStream<WaterSensor, String> sensorKs = sensorDsWithWatermark.keyBy(sensor -> sensor.getId());

        OutputTag<WaterSensor> outputTag = new OutputTag<>("late-data", Types.POJO(WaterSensor.class));

        //水印是针对事件事件的，所以开事件时间窗口
        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
                sensorKs.window(TumblingEventTimeWindows.of(Time.seconds(10)))
                        //延迟2s关窗
                .allowedLateness(Time.seconds(2))
                        //关窗后迟到的数据放入侧输出流
                .sideOutputLateData(outputTag);

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

        process.getSideOutput(outputTag).printToErr("侧输出流");
        //13s时候打印输出
        process.print("主流");
        env.execute();
    }

    /**
     * 乱序、迟到数据的处理
     * 第一招：watermark中指定乱序等待时间
     * 第二招：如果开窗，设置窗口允许迟到
     *        推迟关窗时间，在关窗之前，迟到的数据来了，还能被窗口计算，来一条迟到数据触发一次计算
     *        关窗后，迟到的数据不会被计算
     * 第三招：关窗后迟到的数据，放入侧输出流
     */
}
