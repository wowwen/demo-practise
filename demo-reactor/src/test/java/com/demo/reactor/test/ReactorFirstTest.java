package com.demo.reactor.test;

import com.sun.xml.internal.ws.util.StringUtils;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactorFirstTest {

    private final String[] WORDS = new String[]{"the", "quick", "brown", "fox", "jumped", "over", "the", "lazy", "dog" };

    /**
     * 用Stream方式获取索引
     */
    @Test
    public void testStreamWithIndex() {
        AtomicInteger index = new AtomicInteger(1);
        Arrays.stream(WORDS)
                .map(word -> String.format("%d, %s", index.getAndIncrement(), word))
                .forEach(System.out::println);
    }

    /**
     * 用reactor的方式获取索引
     */
    @Test
    public void testReactorWithIndex() {
        Flux.fromArray(WORDS)                                    //从String数组创建Flux(0-n)的响应流发布者
                .zipWith(Flux.range(1, Integer.MAX_VALUE), //新生成一个整数的Flux响应流，通过zipwith操作符来合并两个流，并将响应流中的元素一一对应（string, int）,当其中一个流完成时，合并结束，为完成的流中的元素将被忽略
                        (word, index) -> String.format("%d, %s", index, word)) //zipwith操作符，支持传递一个BiFunction的函数式接口实现，定于如何来合并两个数据流中的元素，此处用String.format方法将两个流中的字符和索引连接起来
                .subscribe(System.out::println);                //订阅，订阅方法为打印
    }


}
