package com.demo.stream.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.LongSummaryStatistics;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * 流(Stream)是数据渠道，用于操作数据源（集合、数组等）所生成的元素序列。"集合讲的是数据，流讲的是计算！"
 */
@Slf4j
public class StreamTerminalOperationStatisticsPractice {

    /**
     * long count();
     * 返回此流中的元素数。
     */
    @Test
    public void countStream(){
        log.info("[1, 2, 3, 4, 5, 6]元素个数：{}",
                Stream.of(1, 2, 3, 4, 5, 6).count());
    }

    /**
     * Optional min(Comparator comparator);
     * 根据提供的 Comparator返回此流的最小元素。
     */
    @Test
    public void minStream(){
        log.info("[1, 2, 3, 4, 5, 6]的最大值：{}",
                Stream.of(1, 2, 3, 4, 5, 6).min(Comparator.comparingInt(n -> n)).get());
    }

    /**
     * Optional max(Comparator comparator);
     * 根据提供的 Comparator返回此流的最大元素。
     */
    @Test
    public void maxStream(){
        log.info("[1, 2, 3, 4, 5, 6]的最大值：{}",
                Stream.of(1, 2, 3, 4, 5, 6).max(Comparator.comparingInt(n -> n)).get());
    }

    /**
     * OptionalXxx min();
     * 返回 OptionalInt此流的最小元素的OptionalInt，如果此流为空，则返回一个空的可选项。
     */
    @Test
    public void minOfIntStream(){
        log.info("[1, 2, 3, 4, 5, 6]的最小值：{}", IntStream.of(1, 2, 3, 4, 5,6).min().getAsInt());
    }

    /**
     * OptionalXxx max();
     * 返回 OptionalInt此流的最大元素的OptionalInt，如果此流为空，则返回一个空的可选项。
     */
    @Test
    public void maxOfIntStream(){
        log.info("[1, 2, 3, 4, 5, 6]的最小值：{}", IntStream.of(1, 2, 3, 4, 5, 6).max().getAsInt());
    }

    /**
     * OptionalDouble average();
     * 返回 OptionalDouble此流的元素的算术平均值的OptionalDouble，如果此流为空，则返回空的可选项。
     */
    @Test
    public void average(){
        log.info("[1, 2, 3, 4, 5, 6]的平均值：{}",
                IntStream.of(1, 2, 3, 4, 5, 6).average().getAsDouble());
    }

    /**
     * Xxx sum();
     * 返回此流中元素的总和
     */
    @Test
    public void sum(){
        log.info("[1, 2, 3, 4, 5, 6]的求和：{}",
                IntStream.of(1, 2, 3, 4, 5, 6).sum());
    }

    /**
     * XxxSummaryStatistics summaryStatistics();
     * 返回一个 IntSummaryStatistics描述有关此流的元素的各种摘要数据。
     * XxxSummaryStatistics类型的统计对象，如IntSummaryStatistics，
     * 除了提供最小值、最大值、平均值、元素个数、总和外，还提供了accept、combine两个方法，
     * 分别支持添加新的数据和连接另外的统计对象，并自动重新统计结果。
     */
    @Test
    public void summaryStatistics(){
        IntSummaryStatistics summaryStatistics = IntStream.of(1, 2, 3, 4).summaryStatistics();
        log.info("[1, 2, 3, 4]的统计对象：{}", summaryStatistics);//[1, 2, 3, 4]的统计对象：IntSummaryStatistics{count=4, sum=10, min=1, average=2.500000, max=4}
        summaryStatistics.accept(7);
        log.info("添加7后，统计对象变为：{}", summaryStatistics);//IntSummaryStatistics{count=5, sum=17, min=1, average=3.400000, max=7}
        IntSummaryStatistics summaryStatistics1 = IntStream.of(8, 9).summaryStatistics();
        summaryStatistics.combine(summaryStatistics1);
        log.info("合并[8, 9]后，统计对象变为：{}", summaryStatistics);//IntSummaryStatistics{count=7, sum=34, min=1, average=4.857143, max=9}
    }

    @Test
    public void summaryStatisticsLongStream(){
        LongSummaryStatistics longSummaryStatistics = LongStream.of(1, 2, 3).summaryStatistics();
    }
}
