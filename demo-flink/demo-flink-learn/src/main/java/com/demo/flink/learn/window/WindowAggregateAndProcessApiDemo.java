package com.demo.flink.learn.window;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 增量聚合与全窗口函数结合使用
 */
public class WindowAggregateAndProcessApiDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());
        //方便演示，并行度设置为1
        env.setParallelism(1);

        KeyedStream<WaterSensor, String> sensorKs = sensorDs.keyBy(sensor -> sensor.getId());

        WindowedStream<WaterSensor, String, TimeWindow> sensorWs =
                sensorKs.window(TumblingProcessingTimeWindows.of(Time.seconds(20)));

        /**
         * 窗口函数
         * 增量聚合aggregate + 全窗口process
         * 1、增量聚合函数处理数据：来一条计算一条
         * 2、窗口触发时，增量聚合的结果（只有一条）传递给全窗口函数
         * 3、经过全窗口函数的处理包装后，输出
         *
         * 结合的优点
         * 1、增量聚合来一条算一条，存储中间的计算结果，占用空间少
         * 2、全窗口函数可以通过获取上下文，实现灵活的功能
         */
        SingleOutputStreamOperator<String> aggregate = sensorWs.aggregate(
                new MyAggregation(), new MyProcessingWindowFunction()
        );
        aggregate.print();

        env.execute();
    }

    public static class MyAggregation implements AggregateFunction<WaterSensor, Integer, String> {
        /**
         * 创建累加器，初始化累加器。由于Integer的默认为null,为方便计算可以将其初始化为0
         */
        @Override
        public Integer createAccumulator() {
            System.out.println("创建累加器");
            return 0;
        }

        /**
         * 聚合逻辑
         *
         * @param value
         * @param accumulator 之前的聚合结果
         * @return
         */
        @Override
        public Integer add(WaterSensor value, Integer accumulator) {
            System.out.println("调用add():" + value);
            return accumulator + value.getTemperature();
        }

        /**
         * 获取最终结果，窗口触发时输出
         *
         * @param accumulator 聚合结果
         * @return
         */
        @Override
        public String getResult(Integer accumulator) {
            System.out.println("调用getResult（）");
            return accumulator.toString();
        }

        /**
         * 只有会话窗口才会用到，其他窗口不用管
         *
         * @param a
         * @param b
         * @return
         */
        @Override
        public Integer merge(Integer a, Integer b) {
            System.out.println("调用merge方法");
            return null;
        }
    }

    public static class MyProcessingWindowFunction extends ProcessWindowFunction<String, String, String, TimeWindow> {

        @Override
        public void process(String key, Context context, Iterable<String> elements, Collector<String> out) throws Exception {
            //上下文可以拿到window对象，还有侧输出流等
            long start = context.window().getStart();
            long end = context.window().getEnd();
            String startD = DateFormatUtils.format(start, "yyyy-MM-dd HH:mm:ss.SSS");
            String endD = DateFormatUtils.format(end, "yyyy-MM-dd HH:mm:ss.SSS");
            //注意：此时elements中的数据只有一条，就是前面MyAggregation聚合完成之后的结果。
            long count = elements.spliterator().estimateSize();
            out.collect("key=" + key + "[" + startD + "," + endD + ")" + "数目：" + count + "详细：" + elements.toString());
        }
    }

    /**
     * 创建累加器
     * 调用add():WaterSensor{id='w1', ts=1, temperature=1}
     * 调用add():WaterSensor{id='w1', ts=2, temperature=2}
     * 调用add():WaterSensor{id='w1', ts=3, temperature=3}
     * 调用getResult（）
     * key=w1[2025-02-19 21:51:00.000,2025-02-19 21:51:20.000)数目：1详细：[6]
     */
}
