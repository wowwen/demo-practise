package com.demo.stream.test;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class StreamFilterPractice {

    /**
     * Stream distinct();
     * 去重，返回由该流的不同元素（根据 Object.equals(Object) ）组成的流。
     */
    @Test
    public void distinctStream(){
        int[] ints = {1, 2, 2, 2, 3};
        log.info("{}数组去重后的个数{}", Arrays.stream(ints).distinct().count());
    }

    /**
     * Stream filter(Predicatepredicate);
     * 过滤，返回由与此给定谓词匹配的元素组成的流。
     */
    @Test
    public void filterStream(){
        log.info("整数3-250中大于等于21,小于148的奇数有{}个",
                IntStream.range(3, 251).filter(n -> n >= 21 && n < 148 && n % 2 == 1).count());
    }

    /**
     * Stream limit(long maxSize);
     * 限制元素数量，返回截短长度不超过 maxSize 的元素组成的流。
     */
    @Test
    public void limitStream(){
        log.info("随机生成3-50之间的10个正整数，如下：");
        // forEach为终端操作，代表循环消费流中元素
        LongStream.generate(() -> RandomUtil.randomLong(3, 50)).limit(10).forEach(System.out::println);
    }

    /**
     * Stream skip(long n);
     * 跳过头部元素，在丢弃流的第 n 个元素后，返回由该流的元素 n 之后组成的流。
     */
    @Test
    public void skipStream(){
        int[] ints = {1, 2, 3, 4, 5, 1, 2, 3};
        log.info("{}数组去除头部3个元素后变为：{}", ints);
        Arrays.stream(ints).skip(3).forEach(System.out::println);
    }

    /**
     * Stream peek(Consumeraction);
     * 窥探流内元素，返回由该流的元素组成的流，在从生成的流中消耗元素时对每个元素执行提供的操作。
     */
    @Test
    public void peekStream(){
        log.info("打印随机数中的每一个，并计算总和:{}", DoubleStream.generate(RandomUtil::randomDouble).limit(10).peek(System.out::println).sum());
    }

    /**
     * 在1-3000的整数中，70个数为1组，统计第34组的质数个数，并输出他们。
     *
     * 根据题目需求，实际上该题目为分页模型的变体，取出每页70条第34页的质数，分析实现如下：
     *
     * 创建1-3000的整数数字流
     *
     * 忽略前33页的整数
     *
     * 取出第34页的70个整数
     *
     * 过滤为质数的整数
     *
     * 打印他们
     *
     * 输出使用字母个数
     */
    @Test
    public void testPrimesNumber(){
        int page = 33;
        int pageSize = 70;
        int total = 3000;

        log.info("质数个数为：{}", IntStream.range(1, total + 1)
                .skip(page * pageSize)
                .limit(pageSize)
                .filter(NumberUtil::isPrimes)
                .peek(System.out::println)
                .count());
    }


}
