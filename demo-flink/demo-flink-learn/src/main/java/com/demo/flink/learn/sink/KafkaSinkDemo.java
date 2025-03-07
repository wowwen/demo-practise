package com.demo.flink.learn.sink;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import com.demo.flink.learn.transform.MapDemo;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * @author jiangyw
 * @date 2025/2/18 1:46
 * @description 将socket的数据写入kafka
 */
public class KafkaSinkDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<String> sensorDs = env.socketTextStream("192.168.56.141", 8888);
        //方便演示，并行度设置为1
        env.setParallelism(1);
        //如果是精准一次，必须开启checkpoint。如果是至少一次，可以不用开
        env.enableCheckpointing(2000, CheckpointingMode.EXACTLY_ONCE);

        KafkaSink<String> sink = KafkaSink.<String>builder()
                .setBootstrapServers("192.168.56.141:9092")
                //配置序列化器
                .setRecordSerializer(KafkaRecordSerializationSchema
                        .<String>builder()
                        .setTopic("sink-2-kafka")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build())
                //写入kafka的一致性级别：精准一次、至少一次
                .setDeliveryGuarantee(DeliveryGuarantee.EXACTLY_ONCE)
                //如果一致性级别定义为精准一次，则需要开启事务并指定前缀。（如果是至少一次，则可以开启也可以不开启）
                .setTransactionalIdPrefix("sink-kafka-tx-")
                //如果是精准一次，必须设置事务超时时间：需要大于checkpoint间隔，小于max（15分钟）。至少一次则不用设置
                .setProperty(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, 10*60*1000 + "")
                .build();

        sensorDs.sinkTo(sink);

        env.execute();

    }
}
