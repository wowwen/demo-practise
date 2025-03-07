package com.demo.flink.learn.partition;

import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2024/12/17 22:12
 * @description DataSet API（从flink1.12起已过时）
 */
public class PartitionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.141", 8888);
        //shuffle随机分区
        // random.nextInt(numberOfChannels---下游算子并行度)
        socketTextStream.shuffle()
                .print();

        //rebalance轮询,解决数据源倾斜的问题
        //nextChannelToSendTo = (nextChannelToSendTo + 1) % numberOfChannels;
        socketTextStream.rebalance()
                .print();

        //rescale缩放：实现轮询，局部组队，比rebalance更高效。rebalance是再下游算子的所有分区都轮询，rescale只轮询本分组的。
        socketTextStream.rescale()
                .print();

        //broadcast广播：发送给下游的所有子任务
        socketTextStream.broadcast()
                .print();

        //global全局：全部发往第一个子任务.相当于将下游任务的并行度强行变成了1，但是子任务的并行度是没有变的，只是数据全部发往了第一个子任务
        socketTextStream.global()
                .print();

        //还有keyBy:按指定key去发送，相同key发往同一个子任务
        //one-to-one:forward分区器

        //Flink提供了 7种分区器 + 1种自定义分区器（总共8种）

        env.execute();
    }
    /**
     * (flink,1)
     * (world,1)
     * (hello,3)
     * (java,1)
     */
}
