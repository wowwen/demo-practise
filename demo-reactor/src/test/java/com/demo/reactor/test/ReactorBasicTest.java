package com.demo.reactor.test;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ReactorBasicTest {
    /**
     * 测试Just
     */
    @Test
    public void testJust(){
        String str = null;
        Optional<String> optional = Optional.of("hello, mono");
        Mono.just("hello, world").subscribe(System.out::println);
        Mono.justOrEmpty(str).subscribe(System.out::println);
        Mono.justOrEmpty(optional).subscribe(System.out::println);

        Flux.just("hello", " ").subscribe(System.out::println);
        Flux.just("hello").subscribe(System.out::println);
    }

    /**
     * 测试From
     */
    @Test
    public void testFrom(){
        String[] array = {"hello", "reactor", "flux"};
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");

        Flux.fromArray(array).subscribe(System.out::println);
        Flux.fromIterable(iterable).subscribe(System.out::println);
        Flux.fromStream(Arrays.stream(array)).subscribe(System.out::println);
    }

    /**
     * 测试range
     * 从start开始构建一个Flux，该Flux仅发出一系列递增计数的整数。也就是说，在start（包括）和start+count（排除）之间发出整数，然后完成
     */
    @Test
    public void testRange(){
        Flux.range(3, 5).subscribe(System.out::println); //从3开始，数5个数，即3 4 5 6 7，注意是数几个数，不是截止数
    }

    /**
     * 测试interval
     * 在全局计时器上创建一个Flux，该Flux在初始化延迟后，发出从0开始并以指定的时间间隔递增的长整数。如果未及时产生，则会通过溢出IllegalStateException发出onError信号，详细说明无法发出的原因。在正常情况下，Flux将永远不会完成。
     * interval提供了3个重载方法，三者的区别主要在于是否延迟发出、以及使用的调度器
     * Flux<Long> interval(Duration period)                      //没有延迟，按照period的周期立即发出，默认使用Schedulers.parallel() 调度器
     * Flux<Long> interval(Duration delay, Duration period)      //以delay延迟，按照period的周期发出，默认使用Schedulers.parallel() 调度器
     * Flux<Long>interval(Duration delay, Duration period, Scheduler timer)  //以delay延迟，按照period的周期发出，使用指定的调度器
     */
    @Test
    public void testInterval() throws InterruptedException {
        Flux.interval(Duration.ofMillis(3000), Duration.ofMillis(500)).subscribe(System.out::println);
        System.out.println("====");
        Thread.sleep(5000);
        System.out.println("wwwww");
    }

    /**
     * 测试empty
     * 生成一个空的有限流
     */
    @Test
    public void testEmpty(){
        Flux.empty().subscribe(System.out::println, System.out::println, () -> System.out.println("结束"));
    }

    /**
     * 测试错误
     */
    @Test
    public void testError(){
        Flux.error(new IllegalStateException(), true)
                .log()
                .subscribe(System.out::println, System.err::println);
    }

    /**
     * 测试never
     * 生成一个空的无限流
     */
    @Test
    public void testNever(){
        Flux.never().subscribe(System.out::println, System.out::println, ()-> System.out.println("结束"));
    }

    /**
     * 在subscribe订阅中， Flux和Mono支持Java 8 Lambda表达式。下面我们来看看Reactor为我们提供了哪些订阅方法.
     * subscribe(); //序号① 订阅并触发响应式流。
     * subscribe(Consumer<? super T> consumer);序号② 对每个生成的元素进行消费。
     * subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer)；序号③ 对正常元素进行消费，对错误进行响应处理。
     * subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer, Runnable completeConsumer); //序号④ 对正常元素和错误均有响应，还定义了响应流正常完成后的回调
     * subscribe(Consumer<? super T> consumer, Consumer<? super Throwable> errorConsumer, Runnable completeConsumer, Consumer<? super Subscription> subscriptionConsumer); //序号⑤ 对正常元素、错误信号和完成信号均有响应，同时也定义了对该subscribe返回的Subscription的回调处理。
     * subscribe(Subscriber<? super T> actual); //序号⑥ 通过自定义实现 Subscriber 接口来订阅
     *
     * 注意：序号⑤ 变量传递一个 Subscription 的引用，如果不再需要更多元素时，可以通过它来取消订阅。取消订阅时，源头会停止生成数据，并清理相关的资源。取消和清理的操作是在 Disposable 接口中定义的。
     * 注意：序号⑥ 的方式支持背压等操作
     */
    /**
     * 测试Subscribe
     */
    @Test
    public void testSubscribe(){
        Flux.range(1, 4)
                .log()
                .subscribe(System.out::println,
                        error -> System.err.println("error happened:" + error),
                        () -> System.out.println("完成"),
                        sub -> {
                            System.out.println("已订阅");
                            //理解被压
                            //尝试修改下request中的值，看看有啥变化
                            sub.request(5); //比range取值范围下，则只发送此时的请求数；比range取值范围大，则发送range中的请求数
                        });
    }


}
