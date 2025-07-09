package com.demo.stream.test;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Slf4j
public class StreamSortedPractice {

    /**
     * Stream sorted();
     * 正序排序，返回由此流的元素组成的流，根据自然顺序排序。
     */
    @Test
    public void sortedStream(){
        log.info("随机生成3-50之间的20个正整数，排序如下");
        LongStream.generate(() -> RandomUtil.randomLong(3, 50)) //产生的随机数会有重复的
                .limit(20)
                .sorted()
                .forEach(System.out::println);
    }

    /**
     * Comparator.comparingInt(o -> -o)是一个Java 8中的静态方法，用于创建一个比较器(comparator)。
     * 这个比较器的作用是对输入的元素进行整数比较，但是比较的顺序是与原始顺序相反的。
     * 具体来说，comparingInt方法接受一个函数作为参数，该函数将输入的元素映射为一个整数。在这个例子中，使用lambda表达式o -> -o将输入的元素取相反数(-o)并返回。这意味着如果输入的元素是正整数，则返回的结果是负整数；如果输入的元素是负整数，则返回的结果是正整数。如果输入的元素是0，则返回的结果也是0。
     * 因此，这个比较器将按照输入元素的相反数进行比较，从而实现了与原始顺序相反的排序。例如，如果输入的元素是[1, 2, 3]，则排序的结果是[3, 2, 1]。
     * 比较器的排序是返回正数，负数和0
     */
    @Test
    public void sortedComparatorStream(){
        log.info("随机生成3-50之间的20个正整数，倒序排序如下");
        //forEach为终端操作，代表循环消费流中的元素
        Stream.generate(() -> RandomUtil.randomInt(3, 50))
                .limit(20)
                .distinct()
                .sorted(Comparator.comparingInt(o -> -o))
                .forEach(System.out::println);
    }
}
