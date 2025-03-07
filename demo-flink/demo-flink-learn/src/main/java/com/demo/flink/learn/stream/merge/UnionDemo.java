package com.demo.flink.learn.stream.merge;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/17 14:53
 * @description 通过union实现多流合并，流的数据类型必须一致
 */
public class UnionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> source1 = env.fromElements(1, 2, 3, 4);
        DataStreamSource<Integer> source2 = env.fromElements(11, 22, 33, 44);
        DataStreamSource<String> source3 = env.fromElements("111", "222", "333");
        //两种写法
        //要求： 1、流的数据类型必须一致
        //      2、一次可以合并多条流
//        DataStream<Integer> union = source1.union(source2).union(source3.map(s3 -> Integer.valueOf(s3)));

        DataStream<Integer> union = source1.union(source2, source3.map(Integer::valueOf));

        union.print();
        env.execute();
        /**
         * 1
         * 2
         * 3
         * 4
         * 11
         * 22
         * 33
         * 44
         * 111
         * 222
         * 333
         */
    }
}
