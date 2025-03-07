package com.demo.flink.learn.sink;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.connector.source.util.ratelimit.RateLimiterStrategy;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.connector.datagen.source.DataGeneratorSource;
import org.apache.flink.connector.datagen.source.GeneratorFunction;
import org.apache.flink.connector.file.sink.FileSink;
import org.apache.flink.core.fs.Path;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.util.Collector;

import java.time.Duration;
import java.time.ZoneId;

/**
 * @author jiangyw
 * @date 2025/2/14 15:49
 * @description
 */
public class SinkFileDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //如果有N个并行度，最大值为y， 则每个并行度生成数为 y/n, 且从每份的最小值开始
        //写文件时，都会有并行度个数的文件在写入
        env.setParallelism(2);
        //开启checkpoint。如果不开启，文件将一致处于inprogress状态中
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

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
                1000000, //模拟无界流的话，此处填Long.MAX_VALUE
                RateLimiterStrategy.perSecond(100),
                Types.STRING);

        DataStreamSource<String> datagenSource = env.fromSource(dataGeneratorSource,
                WatermarkStrategy.noWatermarks(), "datagen-source");

        FileSink<String> fileSink = FileSink
                //输出行式存储的文件，指定路径，编码.forRowFormat式个泛型方法，需要在之前指定输出类型。
                .<String>forRowFormat(new Path("demo-flink/demo-flink-learn/data"), new SimpleStringEncoder<>("UTF-8"))
                //输出文件的配置：前缀、后缀这些
                .withOutputFileConfig(OutputFileConfig.builder().withPartPrefix("sink").withPartSuffix(".txt").build())
                //指定分桶策略，时区等
                .withBucketAssigner(new DateTimeBucketAssigner<>("yyyy-MM-dd HH", ZoneId.systemDefault()))
                //指定生成的文件的滚动策略
                .withRollingPolicy(DefaultRollingPolicy.builder()
                        //指定时间、大小，两者之间式或的关系，哪一个到了就关闭文件
                        .withRolloverInterval(Duration.ofMinutes(3))
                        .withMaxPartSize(new MemorySize(1024 * 100))
                        .build())
                .build();

        datagenSource.sinkTo(fileSink);

        env.execute();
    }
}
