package com.demo.flink.learn;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2024/12/18 19:52
 * @description 采用DataStream AP处理有界流
 */
public class WordCountStreamDemo {
    public static void main(String[] args) throws Exception {
        //创建执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //read data
        DataStreamSource<String> file = env.readTextFile("demo-flink/demo-flink-learn/data" +
                "/WordCount.txt");
        //deal data
        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMap =
                file.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String word : split) {
                    //转换成二元组
                    Tuple2<String, Integer> tuple2 = Tuple2.of(word, 1);
                    out.collect(tuple2);
                }
            }
        });
        //out data, 根据flatmap的key分组，key为hello这样的字符串
        KeyedStream<Tuple2<String, Integer>, String> keyBy =
                flatMap.keyBy(new KeySelector<Tuple2<String, Integer>, String>() {
            @Override
            public String getKey(Tuple2<String, Integer> value) throws Exception {
                //根据二元组的第一个分组
                return value.f0;
            }
        });
        //聚合,根据二元组的位置聚合，此处是把出现的次数加起来
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyBy.sum(1);
        //输出
        sum.print();
        //execution
        env.execute();
    }
    /**
     * 来一条计算一条，输出一条。流式处理，有状态的计算。
     *
     * 2> (java,1)
     *
     * 5> (world,1)
     * 7> (flink,1)
     *
     * 3> (hello,1)
     * 3> (hello,2)
     * 3> (hello,3)
     */

}
