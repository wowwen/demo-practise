package com.demo.flink.learn.sql;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.TableAggregateFunction;
import org.apache.flink.util.Collector;

import static org.apache.flink.table.api.Expressions.$;
import static org.apache.flink.table.api.Expressions.call;

/**
 * @author jiangyw
 * @date 2025/3/9 5:39
 * @description 自定义表聚合函数
 */
public class MyTableAggFunctionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //TOP-N
        DataStreamSource<Integer> numDs = env.fromElements(1, 4, 6, 3, 4, 5, 13);

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //流转表
        Table table = tableEnv.fromDataStream(numDs, $("num"));
        //注册函数
        tableEnv.createTemporaryFunction("TopN", TopN.class);
        //调用函数，表聚合函数只能用table api
        table.flatAggregate(call("TopN", $("num")).as("value", "rank"))
                .select($("value"), $("rank"))
                .execute()
                .print();

    }

    /**
     * 继承TableAggregateFunction<返回类型， 累加器类型>
     *     返回类型（数值， 排名）==》（13，1）（6，2）
     *     累加器类型（第一名， 第二名）==》（13，6）
     */
    public static  class TopN extends TableAggregateFunction<Tuple2<Integer, Integer>, Tuple2<Integer, Integer>>{

        @Override
        public Tuple2<Integer, Integer> createAccumulator() {
            return Tuple2.of(-1, -1);
        }

        /**
         * 来一条调一次
         * @param acc
         * @param num 每次只进来一个数
         */
        public void accumulate(Tuple2<Integer, Integer> acc, Integer num){
            if (num > acc.f0){
                //新来的变第一，旧的变第二
                acc.f1 = acc.f0;
                acc.f0 = num;

            }else if (num > acc.f1){
                //新的变第二，旧的丢弃
                acc.f1 = num;

            }
        }

        /**
         * 输出结果（数值，排民）
         * @param acc
         * @param out 采集器<返回类型>
         */
        public void emitValue(Tuple2<Integer, Integer> acc, Collector<Tuple2<Integer, Integer>> out){
            if (acc.f0 != -1){
                //第一名
                out.collect(Tuple2.of(acc.f0, 1));
            }
            if (acc.f1 != -1){
                out.collect(Tuple2.of(acc.f1, 2));
            }
        }
    }
    /**
     * +----+-------------+-------------+
     * | op |       value |        rank |
     * +----+-------------+-------------+
     * | +I |           1 |           1 |
     * | -D |           1 |           1 |
     * | +I |           4 |           1 |
     * | +I |           1 |           2 |
     * | -D |           4 |           1 |
     * | -D |           1 |           2 |
     * | +I |           6 |           1 |
     * | +I |           4 |           2 |
     * | -D |           6 |           1 |
     * | -D |           4 |           2 |
     * | +I |           6 |           1 |
     * | +I |           4 |           2 |
     * | -D |           6 |           1 |
     * | -D |           4 |           2 |
     * | +I |           6 |           1 |
     * | +I |           4 |           2 |
     * | -D |           6 |           1 |
     * | -D |           4 |           2 |
     * | +I |           6 |           1 |
     * | +I |           5 |           2 |
     * | -D |           6 |           1 |
     * | -D |           5 |           2 |
     * | +I |          13 |           1 |
     * | +I |           6 |           2 |
     * +----+-------------+-------------+
     * 24 rows in set
     */
}
