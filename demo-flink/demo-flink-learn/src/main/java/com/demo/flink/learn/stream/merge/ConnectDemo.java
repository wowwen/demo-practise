package com.demo.flink.learn.stream.merge;

import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoMapFunction;

/**
 * @author jiangyw
 * @date 2025/2/17 15:47
 * @description 采用connect合并流
 */
public class ConnectDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<Integer> source1 = env.fromElements(1, 2, 3, 4);
        DataStreamSource<String> source3 = env.fromElements("AA", "BB", "CC");

        /**
         * 一次只能链接一条流
         * 流的类型可以不一致
         * 链接后的链接流可以调用map,flatmap,process来处理，但是各个流处理各自的数据
         */
        ConnectedStreams<Integer, String> connect = source1.connect(source3);
        SingleOutputStreamOperator<String> map = connect.map(new CoMapFunction<Integer, String, String>() {
            @Override
            public String map1(Integer value) throws Exception {
                return "From数字流：" + value.toString();
            }

            @Override
            public String map2(String value) throws Exception {
                return "From字母流：" + value;
            }
        });

        map.print();
        env.execute();

    }

}
