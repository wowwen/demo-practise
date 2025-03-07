package com.demo.flink.learn.source;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.file.src.FileSource;
import org.apache.flink.connector.file.src.reader.TextLineInputFormat;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2025/2/13 13:06
 * @description
 */
public class KafkaSourceDemo {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //KafkaSource的builder（）方法是一个泛型方法，查看builder（）方法的源码可以看到需要指定返回类型。所谓泛型方法，是指<OUT> KafkaSourceBuilder<OUT> builder
        // ()返回类型前还有一个泛型
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.56.138:9092")
                .setGroupId("flink-consumer")
                .setTopics("flink_topic")
                //指定反序列化器，这个是只反序列化value。也可以用其他反序列化器，还可以自定义反序列化器.
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();

        //read data，此处是无水印生成策略
//        DataStreamSource<String> fileSource1 = env.fromSource(kafkaSource, WatermarkStrategy.noWatermarks(),
//                "kafkaSource");

        //从source的源头生成水印，从源头source生成水印后就不可以再在流中再用assignTimestampsAndWatermarks指定水印生成
        DataStreamSource<String> fileSource1 = env.fromSource(kafkaSource,
                WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)),
                "kafkaSource");

        fileSource1.print();
        //deal data
        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMap =
                fileSource1.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
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
        //execution
        env.execute();
        /**
         * 输出
         * 1> hello man
         * 3> (hello,1)
         * 3> (man,1)
         */
    }

    /**
     * kafka自身的offset参数
     *     earliest：如果有offset，从offset继续消费；如果没有offset，从最早消费
     *     latest：  如果有offset，从offset继续消费；如果没有offset，从最新消费
     * flink的kafkasource，其offset消费策略于kafka本身的由点区别
     *     OffsetsInitializer.earliest():一定从最早的消费， 默认是earliest
     *     OffsetsInitializer.latest():一定从最新的消费
     */

}
