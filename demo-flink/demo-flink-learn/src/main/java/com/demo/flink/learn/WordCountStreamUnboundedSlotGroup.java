package com.demo.flink.learn;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2024/12/19 19:24
 * @description 采用DataStream API处理无界流（socket通信就可以模拟无界流）
 */
public class WordCountStreamUnboundedSlotGroup {
    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //本地测试用，也带web UI,需要导入依赖
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        //设置全局并行度（所有的算子都是这个并行度）
        env.setParallelism(1);

        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.136", 8888);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = socketTextStream.flatMap(
                //采用lambda表达式的写法（当然，也可以直接写匿名内部类，像之前一样）
                (String value, Collector<String> out) -> {
                    String[] split = value.split(" ");
                    for (String word : split) {
                        out.collect(word);
                    }
                })
                .returns(Types.STRING)
                .map(word -> Tuple2.of(word, 1))
                //设置当前算子（即map算子）的slot共享组，前面的flatmap还是属于默认的default组。所以map和flatmap的子任务，不会共享在同一个slot中。
                .slotSharingGroup("group-1")
                //采用flink的方式解决java lambda表达式的泛型擦除问题。Types.TUPLE(Types.STRING, Types.INT)
                // 括号中传两个参数就是二元组，传三个就是三元组。。。，其中Types.STRING指代Tuple2<String, Integer>中的String，Types.INT指代Integer
                .returns(Types.TUPLE(Types.STRING, Types.INT))
//                .keyBy((Tuple2<String, Integer> value) -> {return value.f0;})
                //等价于上面的lambda表达式
                .keyBy(value -> value.f0)
                .sum(1);

        sum.print();

        env.execute();
    }

    /**
     * slot的特点
     *      1、均分隔离的内存，不隔离CPU
     *      2、可以共享：同一个job中，不同算子的子任务才可以共享同一个slot，且同时运行。
     *         共享的前提是，属于同一个slot共享组，默认都是“default”
     * slot共享的好处
     *      1、将资源密集型和非密集型的任务同时放在一个slot中，他们就可以自行分配对资源的占用比例，从而保证最重的活平均分配给所有的TaskManager。
     *      2、slot共享的另一个好处是允许我们保存完整的作业管道。这样即使某个TaskManager出现故障宕机，其他节点也可以完全不受影响，作业的任务可以继续执行（因为同一个slot中，具有完整的各个算子的子任务）
     *  Flink默认是允许slot共享的，如果希望某个算子对应的任务完全独占一个slot，或者只有某一部分算子共享slot，可以通过设置“slot共享组”手动指定。不同组之间的任务是完全隔离的，必须分配到不同的slot
     *  上。此时，总共需要的slot数量，就是各个slot共享组最大并行度的总和。
     *  slot与并行度的关系
     *      1、slot是一个静态的概念，表示最大的并发上限
     *         并行度是一个动态的概念，表示实际运行占用了几个slot。
 *          2、要求：slot数量 >= job并行度（算子的最大并行度）， job才能运行。
     *          注意：如果是yarn模式，资源是动态申请的。
     *          申请的TM数量 = job并行度/每个TM的slot数，结果向上取整。
     *              比如session（其实per-job也是一样），开始时TM:0 slot:0
     *              提交一个job，并行度10.10/3（假设每个TM配置文件配置的最大slot数为3）=3.3,向上取整，结果就是TM申请4个，slot最终有12个（富余2个）
     *
     */

}
