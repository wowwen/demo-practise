package com.demo.flink.learn.checkpoint;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import static org.apache.flink.streaming.api.environment.ExecutionCheckpointingOptions.ENABLE_CHECKPOINTS_AFTER_TASKS_FINISH;

/**
 * @author jiangyw
 * @date 2024/12/19 19:24
 * @description 保存点savepoint
 */
public class SavepointDemo {
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();

        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(configuration);
        env.setParallelism(2);
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
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

        SingleOutputStreamOperator<String> socketTextStream = env.socketTextStream("192.168.56.141", 8888).uid("socket-id");

        socketTextStream.flatMap(
                //采用lambda表达式的写法（当然，也可以直接写匿名内部类，像之前一样）
                (String value, Collector<Tuple2<String, Integer>> out) -> {
                    String[] split = value.split(" ");
                    for (String word : split) {
                        Tuple2<String, Integer> tuple2 = Tuple2.of(word, 1);
                        out.collect(tuple2);
                    }
                })
                //对于没有设置ID的算子，flink默认会自定进行设置，所以重启后可能会导致id不同而无法兼容以前的状态。所以为了后续维护，强烈建议设置ID
                .uid("save-flatmap")
                .name("flatmap")
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(value -> value.f0)
                .sum(1)
                .uid("sum-id")
                .print()
                .uid("print-id");

        env.execute();
    }
}
