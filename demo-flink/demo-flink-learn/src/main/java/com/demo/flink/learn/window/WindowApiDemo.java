package com.demo.flink.learn.window;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.*;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description
 */
public class WindowApiDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());
        //方便演示，并行度设置为1
        env.setParallelism(1);

        KeyedStream<WaterSensor, String> sensorKs = sensorDs.keyBy(sensor -> sensor.getId());
        //1.1 没有经过keyBy的窗口：窗口内的所有数据进入同一个子任务，并行度强制为1
//        sensorDs.windowAll();
        //1.2 经过keyBy的窗口，每个key上都定义了一组窗口，各自独立地进行统计计算
//        sensorKs.window();

        //基于时间的窗口
//        sensorKs.window(TumblingProcessingTimeWindows.of(Time.seconds(10))); //滚动窗口，窗口长度10s
//        sensorKs.window(SlidingProcessingTimeWindows.of(Time.seconds(10), Time.seconds(5))); //滑动窗口，长度10s， 步长2s
//        sensorKs.window(ProcessingTimeSessionWindows.withGap(Time.seconds(5))); //会话窗口，间隔5s
        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
                sensorKs.window(ProcessingTimeSessionWindows.withDynamicGap(new SessionWindowTimeGapExtractor<WaterSensor>() { //会话窗口，动态调整会话窗口的间隔，每条数据来都会触发调整间隔
            @Override
            public long extract(WaterSensor element) {
                return element.getTs() / 1000L;
            }
        }));

        //基于计数
//        sensorKs.countWindow(5); //滚动窗口，窗口元素为5
//        sensorKs.countWindow(5, 2); //滑动窗口 窗口元素为5. 步长为2
//        sensorKs.window(GlobalWindows.create());//全局窗口，一般不用，需要自定义触发器，驱逐器等，countWindow（）的底层就是用的这个

//        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
//                sensorKs.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));
        //1、增量聚合：来一条数据，计算一条数据，窗口触发的时候输出计算结果
//        sensorWs
//                .reduce()
//        .aggregate()
        //2、全窗口函数：数据来了不计算，存起来，窗口触发的时候，计算并输出结果
//                sensorWs.process()

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
