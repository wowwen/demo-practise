package com.demo.flink.learn.sink;

import com.demo.flink.learn.bean.WaterSensor;
import com.demo.flink.learn.function.WaterSensorFunctionImpl;
import org.apache.flink.connector.jdbc.JdbcConnectionOptions;
import org.apache.flink.connector.jdbc.JdbcExecutionOptions;
import org.apache.flink.connector.jdbc.JdbcSink;
import org.apache.flink.connector.jdbc.JdbcStatementBuilder;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author jiangyw
 * @date 2025/2/18 18:12
 * @description 演示写到Mysql
 */
public class MysqlSinkDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        SingleOutputStreamOperator<WaterSensor> sensorDs = env.socketTextStream("192.168.56.141", 8888).
                map(new WaterSensorFunctionImpl());
        //方便演示，并行度设置为1
        env.setParallelism(1);

        SinkFunction<WaterSensor> sink = JdbcSink.sink("insert into ws values (?, ?, ?)", //以占位符的形式传参
                //此处的<WaterSensor>代表要处理的数据类型
                new JdbcStatementBuilder<WaterSensor>() {
                    @Override
                    public void accept(PreparedStatement preparedStatement, WaterSensor waterSensor) throws SQLException {
                        //parameterIndex代表占位符的位置
                        preparedStatement.setString(1, waterSensor.getId());
                        preparedStatement.setLong(2, waterSensor.getTs());
                        preparedStatement.setInt(3, waterSensor.getTemperature());
                    }
                },
                JdbcExecutionOptions.builder()
                        //批量与时间是或的关系，满足其中一个数据就会写出去了
                        .withBatchSize(100)
                        .withBatchIntervalMs(3000)
                        .withMaxRetries(3)
                        .build(),
                new JdbcConnectionOptions.JdbcConnectionOptionsBuilder()
                        //重试之间的间隔，超过60s就失败
                        .withConnectionCheckTimeoutSeconds(60)
                        .withUrl("jdbc:mysql://127.0.0.1:3306/flink_test?serverTimezone=Asia/Shanghai&useUnicode=true" +
                                "&characterEncoding=UTF-8")
                        .withUsername("root")
                        .withPassword("root")
                        .withDriverName("com.mysql.cj.jdbc.Driver")
                        .build()
        );

        sensorDs.addSink(sink);

        env.execute();
    }
}
