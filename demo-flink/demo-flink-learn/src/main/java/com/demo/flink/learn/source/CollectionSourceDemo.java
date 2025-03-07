package com.demo.flink.learn.source;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @author jiangyw
 * @date 2025/2/13 12:57
 * @description
 */
public class CollectionSourceDemo {
    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> source = env
                //从集合中读
//                .fromCollection(Arrays.asList("哈哈", "嘿嘿", "啊啊"));
                //也可以直接传入元素
                .fromElements("哈哈", "嘿嘿", "啊啊");

        source.print();

        env.execute();

    }
}
