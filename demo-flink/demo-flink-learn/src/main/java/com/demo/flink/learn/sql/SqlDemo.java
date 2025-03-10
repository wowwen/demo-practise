package com.demo.flink.learn.sql;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author jiangyw
 * @date 2025/3/9 5:39
 * @description flink sql demo
 */
public class SqlDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        //写法一
//        //创建配置对象
//        EnvironmentSettings settings = EnvironmentSettings.newInstance()
//                //指定以流模式处理
//                .inStreamingMode()
//                .build();
//        //创建表环境
//        TableEnvironment tableEnv = TableEnvironment.create(settings);

        //写法二，StreamTableEnvironment带了Stream，实际上就已经指明了流模式处理
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);

        //创建表
        tableEnv.executeSql("CREATE TABLE source ( id INT,ts BIGINT,vc INT) \n" +
                "WITH ( \n" +
                "'connector' = 'datagen',\n" +
                "'rows-per-second'='1', \n" +
                "'fields.id.kind'='sequence',\n" +
                "'fields.id.start'='1',\n" +
                "'fields.id.end'='1000',\n" +
                "'fields.ts.kind'='sequence',\n" +
                "'fields.ts.start'='1',\n" +
                "'fields.ts.end'='1000',\n" +
                "'fields.vc.kind'='random',\n" +
                "'fields.vc.min'='1',\n" +
                "'fields.vc.max'='100'\n" +
                ");");

        tableEnv.executeSql("CREATE TABLE sink (id INT,sumVc INT) \n" +
                "WITH ('connector' = 'print' );\n");

        //执行查询
        // 方式1：使用sql进行查询  .sqlQuery()代表查询
        Table table = tableEnv.sqlQuery("select id, sum(vc) as sumVC from source where id > 5 group by id;");
        //如过要使用上面的查询结果，可以将结果table注册成视图
        tableEnv.createTemporaryView("tmp", table);
        tableEnv.sqlQuery("select * from tmp;");

        //方式2：使用table api进行查询
//        Table source = tableEnv.from("source");
//        Table result = source
//                .where($("id").isGreater(5)) //where条件得写在前面
//                .groupBy($("id"))
//                .aggregate($("vc").sum().as("sumVC"))
//                .select($("id"), $("sumVC"));

        //输出结果
        //方式1 sql ,建表和插入 用.executeSql(),查询用.sqlQuery()
        tableEnv.executeSql("insert into sink select * from tmp");
        //方式2 table api
//        result.executeInsert("sink");

    }


}
