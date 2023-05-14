package com.demo.practise.stream.streamtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 我们将终端操作的结果分为如下几类：
 *
 * 匹配
 *
 * 统计
 *
 * 消费
 *
 * 转换
 */
@Slf4j
public class StreamTerminalOperationMatchPractice {

    /**
     * boolean anyMatch(Predicate predicate);
     * 部分匹配，返回此流的任何元素是否与提供的谓词匹配。
     * 注意：如果流为空时，allMatch方法始终返回true；noneMatch方法始终返回true。
     */
    @Test
    public void anyMatchStreamTest(){
        // 是否存在匹配的元素，true
        log.info("[1, 2, 3, 4, 5, 6]存在偶数否：{}",
                Stream.of(1, 2, 3, 4, 5, 6).anyMatch(n -> n % 2 == 0));
    }

    /**
     * boolean allMatch(Predicate predicate);
     * 全部匹配，返回此流的所有元素是否与提供的谓词匹配。
     */
    @Test
    public void allMatchStreamTest(){
        // 全部元素是否都匹配，false
        log.info("[1, 2, 3, 4, 5, 6]全部都是偶数否：{}",
                Stream.of(1, 2, 3, 4, 5, 6).allMatch(n -> n % 2 == 0));
    }

    /**
     * boolean noneMatch(Predicate predicate);
     * 全部不匹配，返回此流中是否没有元素与提供的谓词匹配。
     */
    @Test
    public void noneMatchStreamTest(){
        // 全部元素是否都不匹配，false
        log.info("[1, 2, 3, 4, 5, 6]全部都不是偶数否：{}",
                Stream.of(1, 2, 3, 4, 5, 6).noneMatch(n -> n % 2 == 0));
    }

    /**
     * 空流匹配测试
     */
    @Test
    public void testEmptyStreamMatch() {
        // 空流中不存在任何匹配元素，所以返回false
        log.info("空流是否AnyMatch：{}", Stream.empty().anyMatch(Objects::isNull));
        // 空流中不存在不匹配的，即全部匹配，所以返回true
        log.info("空流是否AllMatch：{}", Stream.empty().allMatch(Objects::isNull));
        // 空流中全部都不匹配，所以返回true
        log.info("空流是否NoneMatch：{}", Stream.empty().noneMatch(Objects::isNull));
    }

    @Test
    public void testEmptyStreamMatchNonNull(){
        boolean b = Stream.empty().allMatch(o -> "aa".equals(o));
        System.out.println(b);
        boolean b1 = Stream.empty().noneMatch(o -> "bb".equals(o));
        System.out.println(b1);
    }

}
