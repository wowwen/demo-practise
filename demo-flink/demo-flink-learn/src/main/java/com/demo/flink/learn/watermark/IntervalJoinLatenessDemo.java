package com.demo.flink.learn.watermark;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/22 14:00
 * @description 窗口连接两条数据流，采用interval join间隔关联偏移量的形式，并且侧输出流输出迟到数据
 * ------low----------up------------ right
 *          \        /
*            \     /
 * ------------Data------------------ left
 * interval join
 * 1、只支持事件事件
 * 2、指定上下界偏移量，负数代表往前，正数代表往后
 * 3、process算子中，只能处理join好了的数据
 * 4、两条流关联后的watermark，以两条流中最小的为准
 * 5、如果当前数据的时间 < 当前的watermark（即上面最小的watermark），就是迟到数据，process将不会处理。
 *    between算子后，可以指定将 左流 或者 右流 迟到的数据放入侧输出流
 */
public class IntervalJoinLatenessDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //方便演示，并行度设置为1
        env.setParallelism(1);

        SingleOutputStreamOperator<Tuple2<String, Integer>> ds1 = env.socketTextStream("192.168.56.141"
                , 8888)
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                         @Override
                         public Tuple2<String, Integer> map(String value) throws Exception {
                             String[] split = value.split(",");
                             return Tuple2.of(split[0], Integer.valueOf(split[1]));
                         }
                     }
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple2<String, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((value, ts) -> value.f1 * 1000)
                );

        SingleOutputStreamOperator<Tuple3<String, Integer, Integer>> ds2 = env.socketTextStream("192.168.56.141"
                , 9999)
                .map(new MapFunction<String, Tuple3<String, Integer, Integer>>() {
                         @Override
                         public Tuple3<String, Integer, Integer> map(String value) throws Exception {
                             String[] split = value.split(",");
                             return Tuple3.of(split[0], Integer.valueOf(split[1]), Integer.valueOf(split[2]));
                         }
                     }
                )
                .assignTimestampsAndWatermarks(
                        WatermarkStrategy.<Tuple3<String, Integer, Integer>>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                                .withTimestampAssigner((value, ts) -> value.f1 * 1000)
                );
        //1、需要现分别做keyby，key其实就是关联条件
        KeyedStream<Tuple2<String, Integer>, String> ks1 = ds1.keyBy(r -> r.f0);
        KeyedStream<Tuple3<String, Integer, Integer>, String> ks2 = ds2.keyBy(r -> r.f0);


        OutputTag<Tuple2<String, Integer>> ks1Tag = new OutputTag<>("ks1-late", Types.TUPLE(Types.STRING, Types.INT));
        OutputTag<Tuple3<String, Integer, Integer>> ks2Tag = new OutputTag<>("ks2-late", Types.TUPLE(Types.STRING, Types.INT, Types.INT));

        SingleOutputStreamOperator<String> intervalJoin = ks1.intervalJoin(ks2)
                //inEventTime() 间隔关联默认的就是事件时间，此方法实际没什么用。还有.inProcessingTime()底下实际上也用的是事件时间。这两个方法都没什么用。
//                .inEventTime()
                //下界偏移2s,上界偏移3s
                .between(Time.seconds(-2), Time.seconds(3))
                .sideOutputLeftLateData(ks1Tag) //需要在between后指定侧输出流
                .sideOutputRightLateData(ks2Tag)
                .process(new ProcessJoinFunction<Tuple2<String, Integer>, Tuple3<String, Integer, Integer>, String>() {
                    //关联上了的数据，未关联上的数据不会进入(未匹配的数据如果需要输出，则采用侧输出流)
                    @Override
                    public void processElement(Tuple2<String, Integer> left, Tuple3<String, Integer, Integer> right,
                                               Context ctx, Collector<String> out) throws Exception {
                        out.collect(left + "=====" + right);
                    }
                });

        intervalJoin.print();

        intervalJoin.getSideOutput(ks1Tag).printToErr("左流：");
        intervalJoin.getSideOutput(ks2Tag).printToErr("右流：");
        env.execute();
    }

}
