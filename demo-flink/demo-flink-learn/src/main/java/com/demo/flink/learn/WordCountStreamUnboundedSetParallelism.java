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
public class WordCountStreamUnboundedSetParallelism {
    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //本地测试用，也带web UI,需要导入依赖
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        //设置全局并行度（所有的算子都是这个并行度）
        env.setParallelism(3);
        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.136", 8888);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = socketTextStream.flatMap(
                //采用lambda表达式的写法（当然，也可以直接写匿名内部类，像之前一样）
                (String value, Collector<Tuple2<String, Integer>> out) -> {
                    String[] split = value.split(" ");
                    for (String word : split) {
                        Tuple2<String, Integer> tuple2 = Tuple2.of(word, 1);
                        out.collect(tuple2);
                    }
                })
                //设置flatmap算子的并行度,在IDEA运行，如果不指定并行度，默认就是电脑的线程数
                .setParallelism(2)
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
     * 并行度优先级（从上到下优先级递减）
     * 1、代码中算子setParallelism()
     * 2、ExecutionEnvironment env.setMaxParallelism()
     * 3、提交任务时设置的Job并行度
     * 4、集群conf配置文件中的parallelism.default（修改后standalone模式的话需要重启一次，配置是启动时候加载的）
     * ps：socket等特殊的IO操作，本身不能并行处理，并行度只能是1
     */
}
