package com.demo.flink.learn;

import org.apache.flink.api.common.typeinfo.TypeHint;
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
public class WordCountStreamUnboundedOperatorChain {
    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //本地测试用，也带web UI,需要导入依赖
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
        //设置全局并行度（所有的算子都是这个并行度）
        env.setParallelism(1);
        //如果不想算子串联到一起，可以设置全局禁用算子链
//        env.disableOperatorChaining();
        DataStreamSource<String> socketTextStream = env.socketTextStream("192.168.56.136", 8888);
        //这里禁用前后算子串联，由于source前面没有算子了，所以之后禁用后面和flatmap的串联。而flatmap与后面的map的串联，不会被禁用
        socketTextStream.disableChaining();
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = socketTextStream.flatMap(
                //采用lambda表达式的写法（当然，也可以直接写匿名内部类，像之前一样）
                (String value, Collector<String> out) -> {
                    String[] split = value.split(" ");
                    for (String word : split) {
                        out.collect(word);
                    }
                })
                //从当前节点开始，开始一个新的链条，与之前的算子一刀两断，之后的算子该串联的还是串联
                .startNewChain()
                //对某一个算子进行打断算子链，包括前面和后面，都不会串联在一起
//                .disableChaining()
                .returns(Types.STRING)
                .map(word -> Tuple2.of(word, 1))
                //采用flink的方式解决java lambda表达式的泛型擦除问题。Types.TUPLE(Types.STRING, Types.INT)
                // 括号中传两个参数就是二元组，传三个就是三元组。。。，其中Types.STRING指代Tuple2<String, Integer>中的String，Types.INT指代Integer
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                //等价于上面的returns写法，这个就是类型提示Type Hints
//                .returns(new TypeHint<Tuple2<String, Integer>>() {})
//                .keyBy((Tuple2<String, Integer> value) -> {return value.f0;})
                //等价于上面的lambda表达式
                .keyBy(value -> value.f0)
                .sum(1);

        sum.print();

        env.execute();
    }

    /**
     * 算子之间的传输关系
     *    一对一
     *    重分区
     * 算子串联的条件
     *    一对一
     *    并行度相同
     *  禁用算子链
     *      1.全局禁用env.disableOperatorChaining();
     *      2.某个算子不参与链化，算子不会与前面的、后面的算子串在一起。A..disableChaining()
     *      3.从某个算子开启新链条,算子A不与前面串联，从A开始正常链化。A.startNewChain()
     *  场景：
     *      优化性能，当前一个算子的逻辑很重，后一个算子的逻辑也很重，则可以考虑拆开，分到不同的线程去处理。
     *      定位问题，算子串联时候不清楚是哪个算子出了问题，禁用可以定位是哪一个算子的问题。
     */

}
