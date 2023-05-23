package com.demo.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * Reactor转换类操作符
 */
public class ReactorTransformOperatorTest {

    /**
     * 测试as操作符：将响应式流转换为目标类型，既可以是非响应式对象，也可以是Flux或者Mono
     */
    @Test
    public void testAs(){
        Flux.range(3, 7)
                .as(Mono::from)
                .subscribe(System.out::println);//输出3
    }

    /**
     * 测试cast操作符：将响应式流内的元素强转为目标类型，如果类型不匹配（非父类类型或者当前类型），则将抛出ClassCastException
     */
    @Test
    public void testCast(){
        Flux.range(1, 3)
                .cast(Number.class)
                .subscribe(System.out::println);
        //输出
        //1
        //2
        //3
    }

    /**
     * Flux独有的collect
     * 通过应用收集器，将Flux发出的所有元素手机到一个容器中。当此流完成时，发出收集的结果
     * Flux提供了2个重载的方法，主要区别在于应用的收集器不同，一个时Java Stream的Collector，另一个是自定义收集方法（同Java Stream中的collect方法）
     * <R, A> Mono<R> collect(Collector<? super T, A, ? extends R> collector)
     * <E> Mono<E> collect(Supplier<E> containerSupplier, BiConsumer<E, ? super T> collector)
     */
    @Test
    public void testCollect(){
        Flux.range(1, 5)
                .collect(Collectors.toList())
                .subscribe(System.out::println);
        //输出
        //[1, 2, 3, 4, 5]
    }

    /**
     * Flux独有的collectList
     * 当此Flux完成时候，将此流发出的所有元素收集到一个列表中，该列表由生成的Mono发出
     */
    @Test
    public void testCollectList(){
        Flux.range(1, 5)
                .collectList()
                .subscribe(System.out::println);
        //输出
        //[1, 2, 3, 4, 5]
    }
}
