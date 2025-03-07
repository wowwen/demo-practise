package com.demo.flink.learn.environment;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2025/2/13 1:09
 * @description
 */
public class EnvDemo {
    public static void main(String[] args) throws Exception {
//        //自动识别是远程环境还是idea本地环境，源码里面实际上是有做远端还是本地的判断的
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        //创建IDEA运行本地环境
//        LocalStreamEnvironment localEnv = StreamExecutionEnvironment.createLocalEnvironment();
//        //创建远端环境
//        StreamExecutionEnvironment remoteEnvironment = StreamExecutionEnvironment.createRemoteEnvironment("hadoop102", 8081,
//                "/XXX");

        //环境的配置对象
        Configuration conf = new Configuration();
        conf.set(RestOptions.BIND_PORT, "8082");

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        //flink是流批一体的，默认是以流的形式处理，如果想改成批形式处理，则设置以下参数为批模式。枚举值有三种
        // RuntimeExecutionMode.BATCH,
        // RuntimeExecutionMode.STREAMING,
        // RuntimeExecutionMode.AUTOMATIC，自动模式下，将由程序根据输入的数据源是否有界，来自动选择执行模式
        // 除了使用代码设置外，还可以通过启动命令参数设置 bin/flink run -Dexecution.runtime-mode=BATCH
//        env.setRuntimeMode(RuntimeExecutionMode.BATCH);

        env.socketTextStream("192.168.56.136", 8888)
                .flatMap(
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
                .sum(1)
                .print();

        //execute()方法并不代表程序结束，只是触发程序执行，flink是事件驱动的，只有数据到来时，才会触发真正的计算（即“延迟执行”或者称“懒执行”）。execute（）方法等待作业完成，然后返回一个执行结果（JobExecutionResult）
        JobExecutionResult execute = env.execute();

//        //一般来说采用env.execute()一个main方法生成一个job，一个main方法可以调用多个env.execute(),
//        //但是没有意义，程序遇到第一个execution方法就会阻塞住，但是如果想生成两个job，也可以替换成下面这种写法，两个executeAsync()
//        // 方法之间可以添加一些其他代码，则前一个job与后一个job会异步执行，不会像execute()一样阻塞。一个main方法里executeAsync()的个数 = 生成的flink job数
          // yarn-application集群，提交一次，集群会有几个flink job？ --->取决于调用了n个executeAsync(),
        // 对应application集群里，会有n个job。对应JobManager中，会有n个JobMaster.
//        env.executeAsync();
//        //其他job2的代码
//        env.executeAsync();
    }
}
