package com.demo.flink.learn.window;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description
 */
public class WindowApiTimeDemo {
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
        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
                sensorKs.window(TumblingProcessingTimeWindows.of(Time.seconds(10)));//滚动窗口，窗口长度10s


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

    /**
     * 1.窗口施恩么时候触发 输出？
     * 时间进展 >= 窗口的最大时间戳（end -1ms）
     *
     * 2.窗口是怎么划分的？
     * start = 向下取整，取窗口时间的整数倍。即如果是13秒到达的数据，建立窗口时候，start=10
     * end = start + 窗口长度
     *
     * 窗口左闭右开  属于本窗口的最大时间戳 = end -1ms
     *
     * 3.窗口的生命周期
     * 创建：属于本窗口的第一条数据来的时候，现new的，放入一个singleton单例的集合中
     * 销毁（关窗）： 时间进展 >= 窗口的最大时间戳（end - 1ms） + 允许迟到的时间（默认0）
     *
     */

}
