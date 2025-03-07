package com.demo.flink.learn.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/14 15:49
 * @description
 */
public class DatagenSourceDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //如果有N个并行度，最大值为y， 则每个并行度生成数为 y/n, 且从每份的最小值开始
        env.setParallelism(2);

        //数据生成器source，第一个参数：实现GeneratorFunction接口，重写map()，输入类型固定是Long（改不了）
        //                 第二个参数：Long类型，自动生成的数字序列（从0自增）的最大值，达到最大值就停止了
        //                 第三个参数：限速策略，比如每秒生成多少个数
        //                 第四个参数： 返回的类型
        DataGeneratorSource<String> dataGeneratorSource = new DataGeneratorSource<>(new GeneratorFunction<Long,
                String>() {
            @Override
            public String map(Long value) throws Exception {
                return "no." + value;
            }
        },
                10, //模拟无界流的话，此处填Long.MAX_VALUE
                RateLimiterStrategy.perSecond(1),
                Types.STRING);

        DataStreamSource<String> datagenSource = env.fromSource(dataGeneratorSource,
                WatermarkStrategy.noWatermarks(), "datagen" +
                "-source");

        //此处可以看出生成数字顺序生成的，但是经过下面的算子处理后，再打印出来的就会变成乱序列的。因为算子的子任务在不同的线程中处理，有快有慢（结合slot，并行度概念思考）
        datagenSource.print();

        //deal data
        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMap =
                datagenSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                        String[] split = value.split(" ");
                        for (String word : split) {
                            //转换成二元组
                            Tuple2<String, Integer> tuple2 = Tuple2.of(word, 1);
                            out.collect(tuple2);
                        }
                    }
                });
        //out data, 根据flatmap的key分组，key为hello这样的字符串
        KeyedStream<Tuple2<String, Integer>, String> keyBy =
                flatMap.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
                    @Override
                    public String getKey(Tuple2<String, Integer> value) throws Exception {
                        //根据二元组的第一个分组
                        return value.f0;
                    }
                });
        //聚合,根据二元组的位置聚合，此处是把出现的次数加起来
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyBy.sum(1);
        //输出
        sum.print();
        env.execute();
    }
}
