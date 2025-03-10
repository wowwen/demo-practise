package com.demo.flink.learn.sql;

import com.demo.flink.learn.bean.WaterSensor;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.annotation.InputGroup;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.ScalarFunction;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author jiangyw
 * @date 2025/3/9 5:39
 * @description 自定义表函数
 */
public class MyTableFunctionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> stringDs = env.fromElements(
                "hello flink",
                "hello world hi",
                "hello java"
        );

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //流转表
        Table table = tableEnv.fromDataStream(stringDs,$("words"));
        tableEnv.createTemporaryView("str", table);
        //注册函数
        tableEnv.createTemporaryFunction("SplitFunction", SplitFunction.class);
        //调用自定义函数
        //交叉连接,固定写法
        tableEnv.sqlQuery("select words, word, length  from str, lateral table(SplitFunction(words))")
                .execute()
                .print();
        //左连接，带on true条件，固定写法
        tableEnv.sqlQuery("select words, word, length  from str left join lateral table(SplitFunction(words)) on true")
                .execute()
                .print();
        //重命名
        tableEnv.sqlQuery("select words, newWord, newLength  from str left join lateral table(SplitFunction(words)) as T" +
                "(newWord, newLength) on true")
                .execute()
                .print();

    }

    /**
     *  继承TableFunction<返回类型> , 返回类型自己写，这里定义为返回最简单的Row
     *  类型要用@DataTypeHint标注。Row包含两个字段：word和length，也就是sql查询语句中查询的字段名称.重命名字段时也可以重命名这里ROW<newWord String, length INT>
     */
    @FunctionHint(output = @DataTypeHint("ROW<word STRING, length INT>"))
    public static class SplitFunction extends TableFunction<Row>{
        //返回必须是void，用collect()方法输出
        public void eval(String str){
            for (String word : str.split(" ")) {
               collect(Row.of(word, word.length()));
            }
        }

    }

    /**
     * +----+--------------------------------+--------------------------------+-------------+
     * | op |                          words |                           word |      length |
     * +----+--------------------------------+--------------------------------+-------------+
     * | +I |                    hello flink |                          hello |           5 |
     * | +I |                    hello flink |                          flink |           5 |
     * | +I |                 hello world hi |                          hello |           5 |
     * | +I |                 hello world hi |                          world |           5 |
     * | +I |                 hello world hi |                             hi |           2 |
     * | +I |                     hello java |                          hello |           5 |
     * | +I |                     hello java |                           java |           4 |
     * +----+--------------------------------+--------------------------------+-------------+
     * 7 rows in set
     * +----+--------------------------------+--------------------------------+-------------+
     * | op |                          words |                           word |      length |
     * +----+--------------------------------+--------------------------------+-------------+
     * | +I |                    hello flink |                          hello |           5 |
     * | +I |                    hello flink |                          flink |           5 |
     * | +I |                 hello world hi |                          hello |           5 |
     * | +I |                 hello world hi |                          world |           5 |
     * | +I |                 hello world hi |                             hi |           2 |
     * | +I |                     hello java |                          hello |           5 |
     * | +I |                     hello java |                           java |           4 |
     * +----+--------------------------------+--------------------------------+-------------+
     * 7 rows in set
     * +----+--------------------------------+--------------------------------+-------------+
     * | op |                          words |                        newWord |   newLength |
     * +----+--------------------------------+--------------------------------+-------------+
     * | +I |                    hello flink |                          hello |           5 |
     * | +I |                    hello flink |                          flink |           5 |
     * | +I |                 hello world hi |                          hello |           5 |
     * | +I |                 hello world hi |                          world |           5 |
     * | +I |                 hello world hi |                             hi |           2 |
     * | +I |                     hello java |                          hello |           5 |
     * | +I |                     hello java |                           java |           4 |
     * +----+--------------------------------+--------------------------------+-------------+
     * 7 rows in set
     */
}
