package com.demo.flink.learn.checkpoint;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.time.Duration;

/**
 * @author jiangyw
 * @date 2024/12/19 19:24
 * @description 保存点savepoint
 */
public class KafkaEosDemo {
    public static void main(String[] args) throws Exception {
        //step1 配置环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        checkpointConfig.setCheckpointStorage("file:///D:\\OWEN\\demo-practise\\demo-flink\\localCheckpointStorge");
        checkpointConfig.setMinPauseBetweenCheckpoints(1000);
        checkpointConfig.setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);

        //step2 读取kafka
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("192.168.56.141:9092")
                .setGroupId("flink-consumer")
                .setTopics("flink_topic")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();

        DataStreamSource<String> fileSource1 = env.fromSource(kafkaSource,
                WatermarkStrategy.forBoundedOutOfOrderness(Duration.ofSeconds(3)),
                "kafkaSource");

        //step3 写出到kafka
        /**
         * 精准一次 写入kafka，需要满足以下条件， 缺一不可
         * 1、开启checkpoint
         * 2、设置事务前缀
         * 3、设置事务超时时间： checkpoint间隔时间 < 事务超时时间 < max的15分钟
         */
        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers("192.168.56.141:9092")
                //配置序列化器
                .setRecordSerializer(KafkaRecordSerializationSchema
                        .<String>builder()
                        .setTopic("sink-2-kafka")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build())
                //精准一次 开启2pc
                .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                //如果一致性级别定义为精准一次，则需要开启事务并指定前缀。（如果是至少一次，则可以开启也可以不开启）
                .setTransactionalIdPrefix("sink-kafka-tx-")
                //如果是精准一次，必须设置事务超时时间：需要大于checkpoint间隔，小于max（15分钟）。至少一次则不用设置
                .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10*60*1000 + "")
                .build();

        fileSource1.sinkTo(sink);

        env.execute();
    }
}
