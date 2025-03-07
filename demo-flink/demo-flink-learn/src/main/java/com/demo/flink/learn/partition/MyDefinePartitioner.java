package com.demo.flink.learn.partition;

import org.apache.flink.api.common.functions.Partitioner;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/16 18:06
 * @description 自定义分区器
 */
public class MyDefinePartitioner {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.141", 8888);
        //KeySelector表示怎样取key， 这里的意思就是将socket输入的数字（这里接收到的类型是字符串）转成Integer
        socketTextStream.partitionCustom(new MyPartitioner(), Integer::parseInt)
                .print();

        env.execute();

    }

    public static class MyPartitioner implements Partitioner<Integer>{
        @Override
        public int partition(Integer key, int numPartitions) {
            //上面设置并行度为2，所以这里的numPartitions=2
            //取模
            System.out.println("使用自定义分区器");
            return key % numPartitions;
        }
    }
}
