package com.demo.practise.stream.streamtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 转换
 */
@Slf4j
public class StreamTerminalOperationTransformPractice {
    /**
     * Optional reduce(BinaryOperator accumulator);
     * 使用associative累积函数对此流的元素执行reduction，并返回描述减小值（如果有的话）的Optional 。
     */
    @Test
    public void reduceTest1(){
        //用reduce方式实现查找最小值
        log.info("[1, 2, 3, 4, 5]的最小值:{}", Stream.of(1, 2, 3, 4, 5).reduce(Integer::min).get());
    }

    /**
     * T reduce(T identity, BinaryOperator accumulator);
     * 使用提供的身份值和 associative累积功能对此流的元素执行 reduction ，并返回减小的值
     */
    @Test
    public void reduceTest2(){
        // 使用reduce方式实现求和
        log.info("[1, 2, 3, 4, 5]求和:{}", Stream.of(1, 2, 3, 4, 5).reduce(0, Integer::sum));
    }

    /**
     * U reduce(U identity, BiFunction accumulator, BinaryOperator combiner);
     * 执行 reduction在此流中的元素，使用所提供的身份，积累和组合功能。(没看懂这啥意思)
     * 此处我们着重说下序号3，带有3个参数的reduce方法，该方法支持转换元素（结果）类型，即从类型T转换为类型U。
     * 第1个参数代表初始值；
     * 第2个参数是累加器函数式接口，输入类型U和类型T，返回类型U；
     * 第3个参数是组合器函数式接口，输入类型U和类型U，返回类型U。
     * 该方法的第3个参数在并行执行下有效。同时需要注意，此方法有如下要求：
     *
     * u = combiner(identity, u);
     *
     * combiner.apply(u, accumulator.apply(identity, t)) == accumulator.apply(u, t);
     */
    @Test
    public void reduceTest3_1(){
        Integer sumInt = Stream.of("aa", "bbb", "c")
                .parallel()
                .reduce(0, //参数1：初始值
                        (sum, str) -> sum + str.length(),//参数2：累加器
                        Integer::sum); //参数3：部分和拼接器，并行执行时才会用到
        // int lengthSum = stream.mapToInt(str -> str.length()).sum();
        log.info("字符串长度：{}", sumInt);
    }

    /**
     * 其他实现方式
     */
    @Test
    public void reduceTest3_2(){
        //下面方法同步执行，能出现正确结果
        //并行执行时，将出现意想不到的结果
        //多线程执行时，append导致初始值identity发生了变化，而多线程又导致了数据重复添加
        StringBuffer wordStb1 = Stream.of("aa", "bbb", "c")
                .parallel()
                .reduce(new StringBuffer(),
                        StringBuffer::append,
                        StringBuffer::append);
        log.info("拼接字符串为：{}", wordStb1);

        //此处如果采用字符串的concat方法，会不停创建字符串常量，导致性能下降
        String wordStr = Stream.of("aa", "bbb", "c")
                .parallel() //同步执行注释该步骤
                .reduce("", //参数1：初始值
                        String::concat, //参数2: 累加器
                        String::concat); //参数3：部分和组合器，并行执行时才会用到
        log.info("拼接字符串为：{}", wordStr);

        //下面方法并行执行时，虽然能达到正确的结果，但并未满足reduce的要求
        List<Integer> accResult  = Stream.of(5, 1, 2, 3, 4)  //如果这里没有指定类型为List<Integer>，则会是List<Object>
                .parallel()
                .reduce(Collections.synchronizedList(new ArrayList<>()),
                        (acc, item) -> {
                            List<Integer> list = new ArrayList<>();
                            list.add(item);
                            System.out.println("item BiFunction:" + item);
                            System.out.println("acc BiFunction:" + list);
                            return list;
                        },
                        (accs, items) -> {
                            accs.addAll(items);
                            System.out.println("item BinaryOperator:" + items);
                            System.out.println("acc+ BinaryOperator:" + accs);
                            return accs;
                        });
        log.info("accResult:{}", accResult);
    }




    @Test
    public void listSort(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(4);
        integers.add(5);
        integers.add(2);
        integers.add(3);
        integers.add(1);
        System.out.println(integers);
    }
}
