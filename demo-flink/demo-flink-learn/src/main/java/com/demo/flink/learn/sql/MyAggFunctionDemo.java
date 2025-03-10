package com.demo.flink.learn.sql;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.annotation.DataTypeHint;
import org.apache.flink.table.annotation.FunctionHint;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.functions.AggregateFunction;
import org.apache.flink.table.functions.TableFunction;
import org.apache.flink.types.Row;

import static org.apache.flink.table.api.Expressions.$;

/**
 * @author jiangyw
 * @date 2025/3/9 5:39
 * @description 自定义聚合函数
 */
public class MyAggFunctionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //求加权平均分
        DataStreamSource<Tuple3<String, Integer, Integer>> scoreDs = env.fromElements(
                Tuple3.of("zhangsan", 80, 3),
                Tuple3.of("zhangsan", 90, 4),
                Tuple3.of("zhangsan",90, 4),
                Tuple3.of("zhangsan",90, 4),
                Tuple3.of("lisi",90, 4),
                Tuple3.of("lisi",90, 4),
                Tuple3.of("lisi",90, 4),
                Tuple3.of("lisi",90, 4)
        );

        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env);
        //流转表
        Table table = tableEnv.fromDataStream(scoreDs, $("f0").as("name"),$("f1").as("score"), $("f2").as("weight"));
        tableEnv.createTemporaryView("scores", table);
        //注册函数
        tableEnv.createTemporaryFunction("WeightAggregateFunction", WeightAggregateFunction.class);
        //调用自定义函数，这里只演示sql方式，table api方式另外写
        //WeightAggregateFunction(score, weight)的传参要和下面accumulate（）方法传进来的参数对齐（排除掉第一个返回类型后的参数顺序）.关键字不能用来做别名
        tableEnv.sqlQuery("select name, WeightAggregateFunction(score, weight) as avg11 from scores group " +
                "by name")
                .execute()
                .print();
    }

    /**
     * 继承AggregateFunction<返回值类型, 累加器ACC类型<加权分数总和，权重总和>>
     * 泛型第一个参数为返回值类型
     * 第二个为累加器ACC类型
     * 这个只是演示自定义聚合函数，权重平均分不一定是这样算的
     */
    public static class WeightAggregateFunction extends AggregateFunction<Double, Tuple2<Integer, Integer>>{

        @Override
        public Double getValue(Tuple2<Integer, Integer> accumulator) {
            return accumulator.f0 * 1D / accumulator.f1;
        }

        @Override
        public Tuple2<Integer, Integer> createAccumulator() {
            return Tuple2.of(0, 0);
        }

        /**
         * 累加计算的方法，每来一条数据都会调用一次
         * @param acc 累加器
         * @param score 第一个参数
         * @param weight 第二个参数
         */
        public void accumulate(Tuple2<Integer, Integer> acc, Integer score, Integer weight){
            //加权总和 = 分数1*权重1 + 分数2 * 权重2...
            acc.f0 += score * weight;
            //权重和
            acc.f1 += weight;
        }
    }
    /**
     * +----+--------------------------------+--------------------------------+
     * | op |                           name |                          avg11 |
     * +----+--------------------------------+--------------------------------+
     * | +I |                           lisi |                           90.0 |
     * | +I |                       zhangsan |                           80.0 |
     * | -U |                       zhangsan |                           80.0 |
     * | +U |                       zhangsan |              85.71428571428571 |
     * | -U |                       zhangsan |              85.71428571428571 |
     * | +U |                       zhangsan |              87.27272727272727 |
     * | -U |                       zhangsan |              87.27272727272727 |
     * | +U |                       zhangsan |                           88.0 |
     * +----+--------------------------------+--------------------------------+
     * 8 rows in set
     */
}
