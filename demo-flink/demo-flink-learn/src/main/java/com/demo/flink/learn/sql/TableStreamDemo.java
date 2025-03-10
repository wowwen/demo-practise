package com.demo.flink.learn.sql;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author jiangyw
 * @date 2025/3/9 5:39
 * @description 表流互转
 */
public class TableStreamDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<WaterSensor> sensorDs = env.fromElements(
                new WaterSensor("s1", 1L, 1),
                new WaterSensor("s2", 2L, 2),
                new WaterSensor("s3", 3L, 3),
                new WaterSensor("s3", 5L, 5),
                new WaterSensor("s4", 6L, 5)
        );

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //流转表
        Table table = tableEnv.fromDataStream(sensorDs);
        tableEnv.createTemporaryView("sensor", table);

        Table filterTable = tableEnv.sqlQuery("select id, ts, temperature from sensor where ts > 3");
        Table sumTable = tableEnv.sqlQuery("select id, sum(temperature) from sensor group by id");

        //表转流
        //1.追加流
        DataStream<WaterSensor> waterSensorDataAppendStream = tableEnv.toDataStream(filterTable, WaterSensor.class);
        waterSensorDataAppendStream.print("filter");

        //2.changelog流（结果需要更新）
        DataStream<Row> rowDataStream = tableEnv.toChangelogStream(sumTable);
        rowDataStream.print("sum");

        //只要代码中调用了DataStream API，就需要execute(),否则不需要
        env.execute();
    }
}
