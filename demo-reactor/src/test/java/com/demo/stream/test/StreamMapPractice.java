package com.demo.stream.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@Slf4j
public class StreamMapPractice {
    /**
     * Stream map(Function mapper);
     * 返回由给定函数mapper应用于此流的元素的结果组成的流。
     */
    @Test
    public void mapStream() {
        log.info("1-20的整数每个增加30后为：");
        IntStream.range(1, 21)
                .map(n -> n + 30)
                .forEach(System.out::println);
    }

    /**
     * IntStream mapToInt(ToIntFunction mapper);
     * 返回一个 IntStream ，其中包含将给定函数应用于此流的元素的结果
     */
    @Test
    public void mapToIntStream() {
        String[] strings = {"1", "2", "3"};
        Arrays.stream(strings).mapToInt(Integer::parseInt).forEach(System.out::println);
    }

    /**
     * LongStream mapToLong(ToLongFunction mapper);
     * 返回一个 LongStream ，其中包含将给定函数应用于此流的元素的结果。
     */
    @Test
    public void mapToLongStream() {
        String[] strings = {"111", "222", "333"};
        Arrays.stream(strings).mapToLong(Long::parseLong).forEach(System.out::println);
    }

    /**
     * DoubleStream mapToDouble(ToDoubleFunction mapper);
     * 返回一个 DoubleStream ，其中包含将给定函数应用于此流的元素的结果。
     */
    @Test
    public void mapToDouble() {
        String[] strings = {"11.1", "22.2", "33.3"};
        Arrays.stream(strings).mapToDouble(Double::parseDouble).forEach(System.out::println);
    }

    /**
     * Stream mapToObj(XxxFunction mapper);
     * 返回一个对象值 Stream ，其中包含将给定函数应用于此流的元素的结果。
     */
    //TODO 举例mapToObj

    /**
     * Stream flatMap(Function> mapper);
     * 返回由通过将提供的映射函数mapper应用于每个元素而产生的映射流的内容来替换该流的每个元素的结果的流。
     */
    @Test
    public void flatMapStream1() {
        //输出1-10中每个奇数的前后及其本身，并求和
        log.info("1-20的中奇数前后元素及求和为:{}", IntStream.range(1, 11)
                .flatMap(n -> n % 2 == 0 ? IntStream.empty() : IntStream.of(n - 1, n, n + 1))
                .peek(System.out::println)
                .sum());
    }

    @Test
    public void flatMapStream2() {
        String str1 = "aaaW";
        String str2 = "bbb Dd";
        String str3 = "ccc123";
        log.info("语句\n[\n{}\n{}\n{}\n]\n使用的字母个数为：{}", str1, str2, str3,
        Stream.of(str1, str2, str3)
                .flatMap(str -> Arrays.stream(str.toLowerCase().split("")))
                .filter(str -> str.matches("\\p{Alpha}"))
                .distinct()
                .peek(System.out::println)
                .count());
    }

    /**
     * IntStream flatMapToInt(Function mapper);
     * 返回一个 IntStream ，其中包含将该流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容的结果。
     */
    @Test
    public void flatMapToIntStream() {
        int int1 = 1;
        int int2 = 2;
        int int3 = 3;

        Stream.of(int1, int2, int3)
                .flatMapToInt(i -> IntStream.of(i + 1))
                .peek(System.out::println)
                .sum();
    }

    /**
     * LongStream flatMapToLong(Function mapper);
     * 返回一个 LongStream ，其中包含将该流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容的结果。
     */
    @Test
    public void flatMapToLongStream(){
        int i1 = 1;
        int i2 = 2;
        int i3 = 3;

        Stream.of(i1, i2, i3)
                .flatMapToLong(i -> LongStream.of(i + 1111111))
                .peek(System.out::println)
                .sum();//流不加终结操作，则前面的peek()中的打印不能打出来
    }

    /**
     * DoubleStream flatMapToDouble(Function mapper);
     * 返回一个 DoubleStream ，其中包含将该流的每个元素替换为通过将提供的映射函数应用于每个元素而产生的映射流的内容的结果。
     */
    @Test
    public void flatMapToDoubleStream(){
        int i1 = 1;
        int i2 = 2;
        int i3 = 3;
        long count = Stream.of(i1, i2, i3)
                .flatMapToDouble(i -> DoubleStream.of(i + 1))
                .peek(System.out::println)
                .count();
        System.out.println("count==="+ count);
    }
    /**
     * 总结：
     * Stream的映射方法主要提供了map和flatMap两个方法，二者的区别为map方法的参数生成的是对象，而flatMap方法的参数生成的是Stream，并将这些生成的Stream连接(concat)起来。flatMap类似于对每个流内元素执行map后的结果执行concat方法。
     * 可以理解为map方法生成的新Stream流和之前的旧Stream流的元素比为 1:1，而flatMap方法每个元素生成的Stream元素个数为零到多个，最终连接 (concat) 后，新旧元素比为 n:1。
     */
}
