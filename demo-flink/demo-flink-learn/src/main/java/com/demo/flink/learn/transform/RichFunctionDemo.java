package com.demo.flink.learn.transform;

import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/14 19:56
 * @description 富文本函数演示
 */
public class RichFunctionDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);
        DataStreamSource<Integer> source = env.fromElements(1, 2, 3, 4);

        /**
         * RichXXXFunction:富函数
         *      1、多了生命周期管理函数
         *          open():每个子任务，在启动时候调用一次
         *          close(): 每个子任务，在销毁时候调用一次。Flink挂掉，是不会调用的。如果正常调用cancel，是会调用的。
         *      2、多了运行时上下文
         */
        source.map(new RichMapFunction<Integer, Integer>() {

            @Override
            public void open(Configuration parameters) throws Exception {
                RuntimeContext runtimeContext = getRuntimeContext();
                int indexOfThisSubtask = runtimeContext.getIndexOfThisSubtask();
                String taskNameWithSubtasks = runtimeContext.getTaskNameWithSubtasks();
                System.out.println(taskNameWithSubtasks + "---"+ indexOfThisSubtask + "---" + "open()");

                super.open(parameters);
            }

            @Override
            public void close() throws Exception {
                RuntimeContext runtimeContext = getRuntimeContext();
                int indexOfThisSubtask = runtimeContext.getIndexOfThisSubtask();
                String taskNameWithSubtasks = runtimeContext.getTaskNameWithSubtasks();
                System.out.println(taskNameWithSubtasks + "---" + indexOfThisSubtask + "---" + "close()");
                super.close();
            }

            @Override
            public Integer map(Integer value) throws Exception {
                return value + 10;
            }
        })
                .print();

        /**
         * Map -> Sink: Print to Std. Out (1/2)#0---0---open()
         * Map -> Sink: Print to Std. Out (2/2)#0---1---open()
         *
         * 1> 12
         * 2> 11
         * 1> 14
         * 2> 13
         *
         * Map -> Sink: Print to Std. Out (2/2)#0---1---close()
         * Map -> Sink: Print to Std. Out (1/2)#0---0---close()
         */

        env.execute();
    }
}
