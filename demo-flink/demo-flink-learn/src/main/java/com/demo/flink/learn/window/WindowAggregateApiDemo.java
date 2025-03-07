package com.demo.flink.learn.window;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 采用aggregate聚合，数据类型可以不一致，且由于有初始化累加器，第一条数据来的时候也会调用add方法。
 */
public class WindowAggregateApiDemo {
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
         * 窗口函数：aggregate增量聚合
         * 1、第一条数据来时，创建窗口，创建累加器，调用add方法
         * 2、来一条算一条，调用一次add方法
         * 3、窗口时间到了，输出时调用一个getResult方法
         * 4、输入、中间累加器、输出 类型可以不一样
         */
        SingleOutputStreamOperator<String> aggregate = sensorWs.aggregate(
                /**
                 * 第一个参数：输入数据的类型
                 * 第二个参数：累加器的类型，存储的中间计算结果的类型
                 * 第三个参数：输出结果的类型
                 */
                new AggregateFunction<WaterSensor, Integer, String>() {
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
        );
        aggregate.print();

        env.execute();
    }

}
