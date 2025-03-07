package com.demo.flink.learn.watermark;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.JoinFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author jiangyw
 * @date 2025/2/22 14:00
 * @description 窗口连接两条数据流
 */
public class WindowJoinDemo {
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
                        Tuple3.of("b", 12, 1),
                        Tuple3.of("c", 13, 1),
                        Tuple3.of("d", 14, 1)
                ).assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, Integer, Integer>>forMonotonousTimestamps()
                                .withTimestampAssigner((value, ts) -> value.f1 * 1000)
                );

        DataStream<String> join = ds1.join(ds2)
                //ds1的keyBy
                .where(r1 -> r1.f0)
                //ds2的keyBy
                .equalTo(r2 -> r2.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                //流1的类型，流2的类型，返回结果类型
                .apply(new JoinFunction<Tuple2<String, Integer>, Tuple3<String, Integer, Integer>, String>() {
                    //已经关联上了的数据，会调用join方法
                    @Override
                    public String join(Tuple2<String, Integer> first, Tuple3<String, Integer, Integer> second) throws Exception {
                        return first + "=====" + second;
                    }
                });

        join.print();

        env.execute();
    }

    /**
     * (a,1)=====(a,1,1)
     * (a,2)=====(a,1,1)
     * (b,3)=====(b,2,1)
     * 从数据流ds1中，（a, 1）与ds2中的("a", 1, 1)匹配，又与("a", 11, 1)匹配，但是("a", 11, 1)不是属于和（a, 1）同一个窗口的，（a, 1）的窗口为【0，10），而("a",
     * 11, 1)属于【10，20）窗口，所以不与("a", 11, 1)匹配。其他同理分析
     */
}
