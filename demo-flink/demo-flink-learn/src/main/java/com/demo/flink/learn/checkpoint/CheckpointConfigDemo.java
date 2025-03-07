package com.demo.flink.learn.checkpoint;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.time.Duration;

import static org.apache.flink.streaming.api.environment.ExecutionCheckpointingOptions.ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH;

/**
 * @author jiangyw
 * @date 2024/12/19 19:24
 * @description 检查点checkpoint配置
 */
public class CheckpointConfigDemo {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        //最终检查点， 1.15开始 默认是true：意思是finished的任务，需要继续参与checkpoint的备份。（对于无界流，可能无所谓，对于有界流，可能有影响）
        configuration.set(ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH, true);

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        env.setParallelism(2);
        //开启changelog。要求checkpoint的最大并发必须为1，其他参数建议在配置文件中取指定
//        env.enableChangelogStateBackend(true);


//        //代码中用到hdfs的话，需要导入hadoop依赖，指定访问hdfs的用户名
//        System.setProperty("HADOOP_USER", "XXX");
        //开启检查点，flink默认是关闭的。检查点默认barrier对齐，周期为5s，精准一次
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
        //指定检查点的存储位置
//        checkpointConfig.setCheckpointStorage("hdfs://192.168.56.141:8020/chk"); //也可以设置成放在本地磁盘
        checkpointConfig.setCheckpointStorage("file:///D:\\OWEN\\demo-practise\\demo-flink\\localCheckpointStorge");
        //checkpint的超时时间：默认10分组
        checkpointConfig.setCheckpointTimeout(60000);//1分钟
        //同时运行中的checkpoint的最大数量
        checkpointConfig.setMaxConcurrentCheckpoints(2);
        //最小等待间隔：上一轮checkpoint结束 到 下一轮checkpoint开始之间的间隔，设置一旦大于0，则上面的设置的checkpoint的最大数量会强制变成1
        checkpointConfig.setMinPauseBetweenCheckpoints(1000);
        //取消作业时（主动cancel），checkpoint的数据是否保留在外部系统。这里配的是删除，则主动cancel是会删掉的，但是程序宕机是不会删除的
        checkpointConfig.setExternalizedCheckpointCleanup(CheckpointConfig.ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION);
        //运行checkpoint连续失败的次数，默认为0（checkpoint一旦失败，job直接挂掉）
        checkpointConfig.setTolerableCheckpointFailureNumber(10);

//        //启用非对齐的检查点保存方式。开启的要求：checkpoint模式必须是精准一次（.enableCheckpointing()），最大并发必须设置为1（setMaxConcurrentCheckpoints()）
//        checkpointConfig.enableUnalignedCheckpoints();
        //开启非对齐检查点才生效： 默认0，意思是一开始就直接用非对齐的检查点；如果大于0， 一开始用 对齐的检查点（barrier对齐），对齐的时间超过这个参数，自动切成 非对齐检查点（非barrier对齐）--高版本才有
//        checkpointConfig.setAlignedCheckpointTimeout(Duration.ofSeconds(1));

        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.141", 8888);

        socketTextStream.flatMap(
                //采用lambda表达式的写法（当然，也可以直接写匿名内部类，像之前一样）
                (String value, Collector<Tuple2<String, Integer>> out) -> {
                    String[] split = value.split(" ");
                    for (String word : split) {
                        Tuple2<String, Integer> tuple2 = Tuple2.of(word, 1);
                        out.collect(tuple2);
                    }
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1)
                .print();

        env.execute();
    }
}
