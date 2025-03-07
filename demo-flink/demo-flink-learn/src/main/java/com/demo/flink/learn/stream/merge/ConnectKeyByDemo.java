package com.demo.flink.learn.stream.merge;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.ConnectedStreams;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * @author jiangyw
 * @date 2025/2/17 17:37
 * @description 合并两条流，匹配流中的数据，类似于inner join的效果
 */
public class ConnectKeyByDemo {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //并行度设置为1，是为了保证所有的数据都去到一个分区里面，实际情况下，不会设置为1，此时，就需要对数据进行keyBy分区，以保证相同id的数据去到同一个分区
//        env.setParallelism(1);
        env.setParallelism(2);

        DataStreamSource<Tuple2<Integer, String>> source1 = env.fromElements(Tuple2.of(1, "A1"),
                Tuple2.of(1, "A2"),
                Tuple2.of(2, "A1"),
                Tuple2.of(3, "A1"));

        DataStreamSource<Tuple3<Integer, String, Integer>> source2 = env.fromElements(Tuple3.of(1, "A2"
                , 11),
                Tuple3.of(1, "B2", 22),
                Tuple3.of(2, "A2", 222),
                Tuple3.of(3, "A3", 333),
                Tuple3.of(4, "A4", 444));

        ConnectedStreams<Tuple2<Integer, String>, Tuple3<Integer, String, Integer>> connect = source1.connect(source2);

        //keyBy需要传两个keySelector,此处根据ID分，直接取ID就行
        ConnectedStreams<Tuple2<Integer, String>, Tuple3<Integer, String, Integer>> cennectKeyBy =
                connect.keyBy(s1 -> s1.f0, s2 -> s2.f0);

        //CoProcessFunction的第三个参数为输出类型
//        SingleOutputStreamOperator<String> process = connect.process(
        SingleOutputStreamOperator<String> process = cennectKeyBy.process(
                new CoProcessFunction<Tuple2<Integer, String>, Tuple3<Integer, String, Integer>, String>() {
                    //这里的写法注意，只能手动写，先写new HashMap<>()然后再返回是不行的，idea不提供这种返回的功能。
                    Map<Integer, List<Tuple2<Integer, String>>> s1Map = new HashMap<>();
                    Map<Integer, List<Tuple3<Integer, String, Integer>>> s2Map = new HashMap<>();

                    @Override
                    public void processElement1(Tuple2<Integer, String> value, Context ctx, Collector<String> out) throws Exception {
                        Integer id = value.f0;
                        if (!s1Map.containsKey(id)) {
                            List<Tuple2<Integer, String>> tuple1s = new ArrayList<>();
                            tuple1s.add(value);
                            //这里不能采用Arrays.asList(),因为Arrays.asList底层是一个定长的数组，不能再add
//                            List<Tuple2<Integer, String>> tuple1s = Arrays.asList(value);
                            s1Map.put(id, tuple1s);
                        } else {
                            List<Tuple2<Integer, String>> tuple2s = s1Map.get(id);
                            tuple2s.add(value);
                        }
                        //去s2Map中找，找到了就输出，没找到就啥也不干
                        if (s2Map.containsKey(id)) {
                            for (Tuple3<Integer, String, Integer> s2Element : s2Map.get(id)) {
                                out.collect("s2:" + s2Element + "-->s1:" + value);
                            }
                        }
                    }

                    @Override
                    public void processElement2(Tuple3<Integer, String, Integer> value, Context ctx,
                                                Collector<String> out) throws Exception {
                        Integer id = value.f0;
                        if (!s2Map.containsKey(id)) {
                            List<Tuple3<Integer, String, Integer>> tuple2s = new ArrayList<>();
                            tuple2s.add(value);
//                            List<Tuple3<Integer, String, Integer>> tuple2s = Arrays.asList(value);
                            s2Map.put(id, tuple2s);
                        } else {
                            List<Tuple3<Integer, String, Integer>> tuple3s = s2Map.get(id);
                            tuple3s.add(value);
                        }
                        //去s1Map中找，找到了就输出，没找到就啥也不干
                        if (s1Map.containsKey(id)) {
                            for (Tuple2<Integer, String> s1Element : s1Map.get(id)) {
                                out.collect("s1:" + s1Element + "====>s2:" + value);
                            }
                        }
                    }
                });

        process.print();

        env.execute();
    }
}
