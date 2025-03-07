package com.demo.flink.learn.watermark;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/22 14:00
 * @description 窗口连接两条数据流，采用interval join间隔关联偏移量的形式
 * ------low--------up------------ right
 *        \        /
 *         \     /
 * ---------Data------------------ left
 */
public class IntervalJoinDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //方便演示，并行度设置为1
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> ds1 =
                env.fromElements(
                        Tuple2.of("a", 1),
                        Tuple2.of("a", 2),
                        Tuple2.of("b", 3),
                        Tuple2.of("c", 4)
                ).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Integer>>forMonotonousTimestamps()
                                .withTimestampAssigner((value, ts) -> value.f1 * 1000)
                );

        SingleOutputStreamOperator<Tuple3<String, Integer, Integer>> ds2 =
                env.fromElements(
                        Tuple3.of("a", 1, 1),
                        Tuple3.of("a", 11, 1),
                        Tuple3.of("b", 2, 1),
                        Tuple3.of("b", 5, 1),
                        Tuple3.of("c", 2, 1),
                        Tuple3.of("d", 14, 1)
                ).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, Integer, Integer>>forMonotonousTimestamps()
                                .withTimestampAssigner((value, ts) -> value.f1 * 1000)
                );

        //1、需要现分别做keyby，key其实就是关联条件
        KeyedStream<Tuple2<String, Integer>, String> ks1 = ds1.keyBy(r -> r.f0);
        KeyedStream<Tuple3<String, Integer, Integer>, String> ks2 = ds2.keyBy(r -> r.f0);

        SingleOutputStreamOperator<String> intervalJoin = ks1.intervalJoin(ks2)
                //inEventTime() 间隔关联默认的就是事件时间，此方法实际没什么用。还有.inProcessingTime()底下实际上也用的是事件时间。这两个方法都没什么用。
//                .inEventTime()
                //下界偏移2s,上界偏移3s
                .between(Time.seconds(-2), Time.seconds(3))
                .process(new ProcessJoinFunction<Tuple2<String, Integer>, Tuple3<String, Integer, Integer>, String>() {
                    //关联上了的数据，未关联上的数据不会进入(未匹配的数据如果需要输出，则采用侧输出流)
                    @Override
                    public void processElement(Tuple2<String, Integer> left, Tuple3<String, Integer, Integer> right,
                                               Context ctx, Collector<String> out) throws Exception {
                        out.collect(left + "=====" + right);
                    }
                });

        intervalJoin.print();

        env.execute();
    }

    /**
     * (a,1)=====(a,1,1)
     * (a,2)=====(a,1,1)  2-2s =0 2+3s =5 窗口为【0，5）
     * (b,3)=====(b,2,1)  3-2s =1 3+3s =6 窗口为【1，6）
     * (b,3)=====(b,5,1)
     * (c,4)=====(c,2,1)  4-2s =2 4+3s =7 窗口为【2，7）
     */
}
