package com.demo.flink.learn.stream.split;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2024/12/17 22:12
 * @description 使用filter实现分流
 */
public class SplitByFilterDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.141", 8888);

        /**
         * 使用filter实现分流，缺点是每个数据要被处理两遍
         */
        //偶数流
        SingleOutputStreamOperator<String> even = socketTextStream.filter(value -> Integer.parseInt(value) % 2 == 0);
        even.print("偶数流");
        //奇数流
        SingleOutputStreamOperator<String> odd = socketTextStream.filter(value -> Integer.parseInt(value) % 2 == 1);
        odd.print("奇数流");

        env.execute();

        /**
         * 奇数流:1> 111
         */
    }

}
