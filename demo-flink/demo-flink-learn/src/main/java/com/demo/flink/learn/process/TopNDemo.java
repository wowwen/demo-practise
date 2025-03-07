package com.demo.flink.learn.process;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimerService;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.util.*;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description 全窗口TopN
 */
public class TopNDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());

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

        sensorDsWithWatermark.windowAll(SlidingEventTimeWindows.of(Time.seconds(10), Time.seconds(5)))
                .process(new MyTopN())
                .print();

        env.execute();
    }

   public static class MyTopN extends ProcessAllWindowFunction<WaterSensor, String, TimeWindow>{

       @Override
       public void process(Context context, Iterable<WaterSensor> elements, Collector<String> out) throws Exception {
           Map<Integer, Integer> map = new HashMap<>();

           for (WaterSensor element : elements) {
               if (map.containsKey(element.getTemperature())){
                   map.put(element.getTemperature(), map.get(element.getTemperature()) + 1);
               }
               else{
                   map.put(element.getTemperature(), 1);
               }
           }
            //排序。利用list来实现
           List<Tuple2<Integer, Integer>> tuple2s = new ArrayList<>();
           for (Integer key : map.keySet()) {
               tuple2s.add(Tuple2.of(key, map.get(key)));
           }
           tuple2s.sort(new Comparator<Tuple2<Integer, Integer>>() {
               @Override
               public int compare(Tuple2<Integer, Integer> o1, Tuple2<Integer, Integer> o2) {
                   //降序  后 减 前， 升序： 前 - 后
                   return o2.f1 - o1.f1;
               }
           });
           //取出前两个
           StringBuilder outStr = new StringBuilder();
           //遍历，list中的数目可能不足2，取最小的
           for (int i = 0; i < Math.min(2, tuple2s.size()) ; i++) {
               Tuple2<Integer, Integer> tuple2 = tuple2s.get(i);
               outStr.append("TOP " + i + 1);
               outStr.append("\n");
               outStr.append("ID:" + tuple2.f0 + " 次数:" + tuple2.f1);
               outStr.append("\n");
               outStr.append("窗口结束时间：" + DateFormatUtils.format(context.window().getEnd(), "yyyy-MM-dd HH:mm:ss.SSS"));
           }
           out.collect(outStr.toString());
       }
   }
}
