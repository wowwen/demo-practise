package com.demo.practise.stream.streamtest;

import cn.hutool.core.lang.hash.Hash32;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StreamTerminalOperationCollectorPractice {
    /**
     * R collect(Supplier supplier, BiConsumer accumulator, BiConsumer combiner);
     * 对此流的元素执行 mutable reduction操作。
     * 传递了3个参数，
     * 参数1为创建新结果容器的函数；
     * 参数2为累加器函数，将参数1和流内元素执行累加操作；
     * 参数3为组合器函数，并行执行时会使用该函数
     */
    @Test
    public void collect_1() {
        //串行，参数3无用
        //同步执行时，该方法相当于
        //R result = supplier.get()
        //for(T element : this Stream){
        //  accumulator.accept(result, element);
        // }
        //return result;
        log.info("拼接字符串为：{}",
                Stream.of("a", "b", "c", "d")
                        .collect(StringBuilder::new,
                                (b1, b2) -> {
                                    log.info("累计执行：{} + {}", b1, b2);
                                    b1.append(b2);
                                },
                                (b1, b2) -> {
                                    log.info("组合执行：{} + {}", b1, b2);
                                    b1.append(b2);
                                }
                        ).toString());
    }

    //会按顺序输出，为什么？
    //这段代码会按顺序输出abcdfe的结果，是因为在并行流操作中使用了StringBuilder::new来初始化StringBuilder，
    // 而StringBuilder是一个非线程安全的类，因此在进行并行操作时需要确保多线程之间的同步，
    // 所以collect方法内部使用了线程安全的累加器来保证多线程之间的同步，
    // 而这个累加器在内部实现中是一个线程安全的StringBuilder，
    // 它会在进行累计和组合操作时，使用了synchronized来保证线程安全，因此最终的结果是有序的。
    @Test
    public void collect_2() {
        //并行执行时，该方法相当于
        //R result1 = supplier.get();
        //R result2 = supplier.get();
        //R result3 = supplier.get();
        //R result4 = supplier.get();
        //累加执行，此处为并发（多线程）执行，每行代表一个线程
        //accumulator.accept(result1, element1);
        //accumulator.accept(result2, element2);
        //accumulator.accept(result3, element3);
        //accumulator.accept(result4, element4);
        //...
        ////accumulator.accept(resultN, elementN)
        //开始组合，此处为并发执行（多线程），每行代表一个线程
        //combiner.accept(result1, result2);
        //combiner.accept(result3, result4);
        //combiner.accept(result1, result3);
        ////combiner.accept(result1, resultN);
        //return result1;
        for (int i = 0; i < 500; i++) {
//            String s = Stream.of("a", "b", "c", "d", "f", "e")
//                    .parallel()
//                    .collect(StringBuilder::new,
//                            (b1, b2) -> {
//                                b1.append(b2);
//                            },
//                            (b1, b2) -> {
//                                b1.append(b2);
//                            }
//                    ).toString();
//            System.out.println("测试顺序行：" + s);

            log.info("拼接字符串为：{}",
                    Stream.of("a", "b", "c", "d", "f", "e")
                            .parallel()
                            .collect(StringBuilder::new,
                                    (b1, b2) -> {
                                        log.info("累计执行：{} + {}", b1, b2);
                                        b1.append(b2);
                                    },
                                    (b1, b2) -> {
                                        log.info("组合执行：{} + {}", b1, b2);
                                        b1.append(b2);
                                    }
                            ).toString());
            //注意：上述日志中出现的ForkJoinPool.commonPool-worker-N为并发（多线程）执行时的线程名
        }
//        log.info("拼接字符串为：{}",
//                Stream.of("a", "b", "c", "d", "f", "e")
//                        .parallel()
//                        .collect(StringBuilder::new,
//                                (b1,b2) -> { log.info("累计执行：{} + {}", b1, b2); b1.append(b2);},
//                                (b1,b2) -> { log.info("组合执行：{} + {}", b1, b2); b1.append(b2);}
//                        ).toString());
//        //注意：上述日志中出现的ForkJoinPool.commonPool-worker-N为并发（多线程）执行时的线程名
    }

    @Test
    public void collect_3() {
        //使用collect方法实现集合连接
        log.info("拼接集合为：{}",
                Stream.of("a", "b", "c")
                        .parallel()
                        .collect(ArrayList::new, ArrayList::add, ArrayList::addAll)
                        .toString());
    }

    /**
     * collector接口实现
     * 实现collector需要实现如下4个接口
     * 1.一个创建并返回一个新的可变结果容器的函数
     * Supplier<A> supplier;
     * 2.将值折叠成可变结果容器的函数
     * BiConsumer<A, T> accumulator();
     * 3.一个接受两个部分结果并将其合并的函数
     * BinaryOperator<A> combiner();
     * 4.执行从中间累加类型A到最终结果类型R的最终函数
     * Function<A, R> finisher();
     * 5.返回一个Collector.Characteristics 类型的Set，表示该收集容器的特征
     * collect方法执行时，他们的调度流程如下：
     * 1.创建新的结果容器（supplier（））
     * 2.将新的数据元素并入结果容器（accumulator（））--累加器
     * 3.将两个结果容器组合成一个（combiner（））
     * 4.在容器上执行可选的最终变换（finisher()）
     * 简单来讲，生成容器A，通过accumulator针对A及流元素T执行累加，（如果并行存在的话）对多个A执行组合combiner，
     * 最终执行finisher后由A转换为R。对于使用者来说，A为中间变量，无关其实现细节。
     */
    @Test
    public void collect_4() {
        //我们实现一个计算整数流的平均数的collector，代码如下
        log.info("[1, 2, 3, 4, 5, 6]的平均值为：{}",
                Stream.of(1, 2, 3, 4, 5, 6)
                        .parallel()
                        .collect(new Collector<Integer, long[], Double>() {
                            @Override
                            public Supplier<long[]> supplier() {
                                return () -> new long[2];
                            }

                            @Override
                            public BiConsumer<long[], Integer> accumulator() {
                                return (a, t) -> {
                                    log.info("{}累加{}", a, t);
                                    log.info("加t之前a[0]为：", a[0]);
                                    a[0] += t;
                                    log.info("加t之后a[0]为:", a[0]);
                                    log.info("加t之后a[0]为:", a[0]);
                                    a[1]++;
                                };
                            }

                            @Override
                            public BinaryOperator<long[]> combiner() {
                                return (a, b) -> {
                                    log.info("{}组合{}", a, b);
                                    a[0] += b[0];
                                    a[1] += b[1];
                                    return a;
                                };
                            }

                            @Override
                            public Function<long[], Double> finisher() {
                                return (a) -> a[1] == 0 ? 0 : new Long(a[0]).doubleValue() / a[1];
                            }

                            @Override
                            public Set<Characteristics> characteristics() {
                                Set<Characteristics> set = new HashSet<>();
                                set.add(Characteristics.CONCURRENT);
                                return set;
                            }
                        })
        );
    }

    /**
     * 常用Collector：Java 开发者们更为贴心的为我们创建了一些常用的 Collector ，让我们可以直接使用。这些常用的 Collector 实现放在 Collectors 类下，我们来了解下。
     *
     */
    /**
     * 统计平均值averagingXXX
     * Collectors提供了averagingDouble、averagingLong、averagingInt共3种统计平均值的Collector实现类
     */
    @Test
    public void averagingInt() {
        //使用collector实现求平均值
        log.info("[1, 2, 3, 4, 5, 6]的平均值：{}",
                Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.averagingInt(n -> n)));
    }

    /**
     * 统计元素个数counting的使用
     * 该方法和Stream中的count()方法一样
     */
    @Test
    public void counting() {
        log.info("[1, 2, 3, 4, 5, 6]的个数：{}, {}",
                //第一种写法
                Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.counting()));
                //第二种写法
                Stream.of(1, 2, 3, 4, 5, 6).count();
    }

    /**
     * 统计中和summingXXX的使用
     * Collectors提供了summingDouble，summingLong， summingInt三种统计求和值的Collectors实现类，
     * 同时还提供了summarizingDouble，summarizingLong，summarizingInt三种统计对象的Collectors实现类
     */
    @Test
    public void summingInt(){
        //使用Collector获取总和
        log.info("[1, 2, 3, 4, 5, 6]的总和：{}",
                Stream.of(1, 2, 3, 4, 5, 6).collect(Collectors.summingInt(n -> n)));
    }

    /**
     * 统计最小元素minBy()的使用
     */
    @Test
    public void minBy(){
        //使用
    }

}
