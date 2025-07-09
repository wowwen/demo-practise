package com.demo.stream.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;

/**
 * 消费:
 * 消费类型的终端操作是对流内元素的获取或循环消费
 */
@Slf4j
public class StreamTerminalOperationConsumePractice {
    /**
     * Optional findFirst();
     * 返回描述此流的第一个元素的Optional，如果流为空，则返回一个空的Optional。
     */
    @Test
    public void findFirstStreamTest() {
        for (int i = 0; i < 1000; i++) {
            log.info("[1, 2, 3, 4]的首个值：{}", Stream.of(1, 2, 3, 4).parallel().findFirst().get());
        }
    }

    /**
     * Optional findAny();
     * 返回描述流的一些元素的Optional,如果流为空，则返回一个空的Optional。
     */
    @Test
    public void findAnyTest(){
        log.info("1, 2, 3, 4的任意值：{}", Stream.of(1, 2, 3, 4).parallel().findAny().get());
    }

    /**
     * void forEach(Consumer action);
     * 对此流的每个元素执行操作。
     */
    @Test
    public void forEachTest(){
        //parallel()并行后顺序随机，输出不保证顺序
        Stream.of(1, 2, 3, 4, 5).parallel().forEach(System.out::println);
    }

    /**
     * void forEachOrdered(Consumer action);
     * 如果流具有定义的顺序，则以流的顺序对该流的每个元素执行操作
     */
    @Test
    public void forEachOrdered(){
        // 无论是否并行，始终按照流定义的顺序或排序后的结果输出
        log.info("[1, 2, 5, 4, 3]的并行后顺序循环输出");
        Stream.of(1, 2, 5, 4, 3).sorted().parallel().forEachOrdered(System.out::print);
    }
}
