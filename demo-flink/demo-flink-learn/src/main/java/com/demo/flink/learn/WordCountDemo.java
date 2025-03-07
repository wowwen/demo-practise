package com.demo.flink.learn;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author jiangyw
 * @date 2024/12/17 22:12
 * @description DataSet API（从flink1.12起已过时）
 */
public class WordCountDemo {
    public static void main(String[] args) {
        //创建执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //读取数据（本次从文件中读取），鼠标右键可以复制相对路径或者是全路径
        DataSource<String> fileLine = env.readTextFile("demo-flink/demo-flink-learn/data/WordCount.txt");
        //数据切分，转换
        FlatMapOperator<String, Tuple2<String, Integer>> map =
                fileLine.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String lineStr, Collector<Tuple2<String, Integer>> collector) throws Exception {
                //按字符空格切割单词
                String[] words = lineStr.split(" ");
                //将数据转换成（word， 1）的形式
                for (String word : words) {
                    Tuple2<String, Integer> tp2 = Tuple2.of(word, 1);
                    //使用收集器Collector向下游发送数据
                    collector.collect(tp2);
                }
            }
        });
        //按照单词进行分组（map里面实际上是【hello，1】，【world，1】，【hello, 1】,【java， 1】，【hello， 1】， 【flink， 1】）
        UnsortedGrouping<Tuple2<String, Integer>> groupByWord = map.groupBy(0);
        //各分组聚合
        AggregateOperator<Tuple2<String, Integer>> sum = groupByWord.sum(1);
        //输出
        try {
            sum.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * (flink,1)
     * (world,1)
     * (hello,3)
     * (java,1)
     */
}
