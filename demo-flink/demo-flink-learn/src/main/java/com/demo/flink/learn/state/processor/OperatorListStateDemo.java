package com.demo.flink.learn.state.processor;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author jiangyw
 * @date 2025/2/25 20:48
 * @description 算子状态的 ListState
 * 在map算子中统计数据的个数
 */
public class OperatorListStateDemo {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(2);

        env.socketTextStream("192.168.56.141", 8888)
                .map(new MyOperatorStatMapFunction())
                .print();
    }

    //算子状态需要实现CheckpointedFunction接口
    public static class MyOperatorStatMapFunction implements MapFunction<String, Long>, CheckpointedFunction {
        private Long count = 0L;
        private ListState<Long> listState;

        @Override
        public Long map(String value) throws Exception {
            return ++count;
        }

        /**
         * 将本地变量持久化， 将本地变量copy到算子状态中，只有开启checkpoint时才会调用
         *
         * @param context
         * @throws Exception
         */
        @Override
        public void snapshotState(FunctionSnapshotContext context) throws Exception {
            System.out.println("snapshotState......");
            //先清除
            listState.clear();
            //再将本地变量添加到算子状态中
            listState.add(count);
        }

        /**
         * 程序故障恢复时，从算子状态中把数据添加到本地变量中，每个子任务调用一次
         *
         * @param context
         * @throws Exception
         */
        @Override
        public void initializeState(FunctionInitializationContext context) throws Exception {
            System.out.println("initializeState......");
            //1.从上下文中 初始化算子状态
            listState = context.getOperatorStateStore()
                    //列表状态List State
                    .getListState(new ListStateDescriptor<Long>("operator-list", Types.LONG));
            //联合列表unionlist state
//            .getUnionListState(new ListStateDescriptor<Long>("operator-union-list", Types.LONG));
            //2.从算子状态中，把数据copy到本地变量
            if (context.isRestored()) {
                for (Long aLong : listState.get()) {
                    System.out.println("算子状态：" + aLong);
                    count += aLong;
                }
            }

        }
    }

    /**
     * 算子状态中 List State 与 UnionList State 的区别
     * 1、List State： 轮询分配给新的子任务
     * 2、unionlist state: 先将多个子任务的状态合并成一个完整的列表，然后将完整的列表广播发送给新的并行子任务，每一个子任务的都是一份完整的
     */
}

