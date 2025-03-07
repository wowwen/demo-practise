package com.demo.flink.learn.checkpoint;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.IsolationLevel;

import java.time.Duration;
import java.util.Locale;

/**
 * @author jiangyw
 * @date 2025/3/2 0:46
 * @description 消费KafkaEosDemo中通过二阶段（2pc）提交的数据
 */
public class Kafka2PCConsumer {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.56.141:9092")
                .setGroupId("flink-consumer")
                .setTopics("sink-2-kafka")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                //默认kafka的消费者， 隔离级别时读 未提交，所以预提交阶段的数据也会被读到。为保证端到端的精准一次，消费者需要设置为  读已提交
                .setProperty(ConsumerConfig.ISOLATION_LEVEL_CONFIG,
                        IsolationLevel.READ_COMMITTED.toString().toLowerCase(Locale.ROOT))
                .build();

         env.fromSource(kafkaSource,
                WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)),
                "kafkaSource")
                .print("消费2PC");

         env.execute();

    }
}
