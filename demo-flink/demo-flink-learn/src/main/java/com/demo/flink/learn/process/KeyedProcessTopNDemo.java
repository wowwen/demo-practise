package com.demo.flink.learn.process;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.*;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 使用keyby实现topN,
 */
public class KeyedProcessTopNDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888)
                .map(new WaterSensorFunctionImpl());

        WatermarkStrategy<WaterSensor> watermarkStrategy = WatermarkStrategy
                //指定Watermark的生成，乱序的，等待3s
                .<WaterSensor>forBoundedOutOfOrderness(Duration.ofSeconds(3))
                //指定时间戳分配器，从数据中提取
                .withTimestampAssigner((element, recordTimestamp) ->
                        //返回的时间戳，毫秒
                        element.getTs() * 1000
                );

        SingleOutputStreamOperator<WaterSensor> sensorDsWithWatermark =
                sensorDs.assignTimestampsAndWatermarks(watermarkStrategy);

        //1、按照温度分组， 开窗， 聚合（增量计算+全量打标签）
        //开窗聚合后返回的是SingleOutputStreamOperator,继承自DataStream，已无窗口信息，所以需要打赏窗口标签，区分各个窗口的统计数据在各个窗口中（详情见算子和流的流转图）
        SingleOutputStreamOperator<Tuple3<Integer, Integer, Long>> winAgg =
                sensorDsWithWatermark.keyBy(sensor -> sensor.getTemperature())
                        //开窗
                        .window(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                        //聚合
                        .aggregate(new TopNAggregate(), new WindowResult());

        winAgg.print("开窗聚合");
        //2、按照窗口标签进行keyBy,保证同一个窗口时间范围的结果数据分到一起，然后排序，取topN
        winAgg.keyBy(r -> r.f2)
                .process(new TopNProcessFunction(2))
                .print("结果");

        env.execute();
    }


    public static class TopNAggregate implements AggregateFunction<WaterSensor, Integer, Integer> {

        @Override
        public Integer createAccumulator() {
            //累加器初始化为0
            return 0;
        }

        @Override
        public Integer add(WaterSensor value, Integer accumulator) {
            //来一个数据加1，数据是根据温度来分组进来的
            return accumulator + 1;
        }

        @Override
        public Integer getResult(Integer accumulator) {
            return accumulator;
        }

        @Override
        public Integer merge(Integer a, Integer b) {
            return null;
        }
    }

    /**
     * 第一个参数：输入类型 = 增量函数的输出 count值 Integer
     * 第二个参数：输出类型 = Tuple3《temperature， count， windowendstamp》,只所以要带上窗口标签（windowendstamp），是为了后面区分每个窗口的topN
     * 第三个参数：key的类型，temperature
     * 第四个参数：窗口类型
     */
    public static class WindowResult extends ProcessWindowFunction<Integer, Tuple3<Integer, Integer, Long>, Integer,
            TimeWindow> {

        @Override
        public void process(Integer key, Context context, Iterable<Integer> elements, Collector<Tuple3<Integer,
                Integer, Long>> out) throws Exception {
            Integer count = elements.iterator().next();
            out.collect(Tuple3.of(key, count, context.window().getEnd()));
        }
    }

    public static class TopNProcessFunction extends KeyedProcessFunction<Long, Tuple3<Integer, Integer, Long>, String> {
        //每一个窗口值windowendstamp，有多个结果 top1 top2 top3...
        private Map<Long, List<Tuple3<Integer, Integer, Long>>> map;
        //阈值 topN N为多少
        private Integer threshold;

        public TopNProcessFunction(Integer threshold) {
            this.threshold = threshold;
            map = new HashMap<>();
        }

        @Override
        public void processElement(Tuple3<Integer, Integer, Long> value, Context ctx, Collector<String> out) throws Exception {
            //进入这个方法，只有一条数据，要排序，得到齐所有数据才行，所以需要将数据存起来，不同得窗口分开存
            Long wend = value.f2;
            if (map.containsKey(wend)) {
                map.get(wend).add(value);
            } else {
                List<Tuple3<Integer, Integer, Long>> list = new ArrayList<>();
                list.add(value);
                map.put(wend, list);
            }

            //注册定时器，end + 1ms即可
            //同一个窗口范围，应该同时输出，只不过是一条一条调用processElement方法，只需要延迟1ms即可搜集齐所有本窗口得数据了
            ctx.timerService().registerEventTimeTimer(wend + 1);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            super.onTimer(timestamp, ctx, out);
            Long wend = ctx.getCurrentKey();
            List<Tuple3<Integer, Integer, Long>> tuple3s = map.get(wend);
            tuple3s.sort(new Comparator<Tuple3<Integer, Integer, Long>>() {
                @Override
                public int compare(Tuple3<Integer, Integer, Long> o1, Tuple3<Integer, Integer, Long> o2) {
                    return o2.f1 - o1.f1;
                }
            });

            //取出前两个
            StringBuilder outStr = new StringBuilder();
            //遍历，list中的数目可能不足2，取最小的
            for (int i = 0; i < Math.min(threshold, tuple3s.size()); i++) {
                Tuple3<Integer, Integer, Long> tuple2 = tuple3s.get(i);
                outStr.append("TOP " + i + 1);
                outStr.append("\n");
                outStr.append("ID:" + tuple2.f0 + " 次数:" + tuple2.f1);
                outStr.append("\n");
                outStr.append("窗口结束时间：" + DateFormatUtils.format(ctx.timestamp(), "yyyy-MM-dd HH:mm:ss.SSS"));
            }
            out.collect(outStr.toString());

            tuple3s.clear();
        }
    }
}
