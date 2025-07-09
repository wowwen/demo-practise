package com.demo.reactor.test;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.tools.agent.ReactorDebugAgent;

import java.time.Duration;
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

    /**
     * 使用stepVerifier测试
     */
    @Test
    public void testStepVerifier(){
        //创建Flux响应流
        Flux<String> flux = Flux.just("foo", "bar");
        //使用concatWith操作符连接2个响应流
        Flux<String> boom = flux.concatWith(Mono.error(new IllegalArgumentException("boomboom")));
        //创建一个StepVerify构造器来包装和校验一个flux
        Duration verify = StepVerifier.create(boom)
                //第一个我们期望的信号时onNext，它的值为foo
                .expectNext("foo")
                //第二个我们期望的信号是onNext,它的值为bar
                .expectNext("bar")
                //最后我们期望的是一个终止信号onError,异常内容应该为boom,如果非boom，则会抛异常
                .expectErrorMessage("boom")
                //使用verify()触发测试
                .verify();
        System.out.println("==");
    }
    /**
     * 除了正常测试外，Reactor 还提供了诸如：
     *
     * 1.测试基于时间操作符相关的方法，使用 StepVerifier.withVirtualTime 来进行
     *
     * 2.使用 StepVerifier 的 expectAccessibleContext 和 expectNoAccessibleContext 来测试 Context
     *
     * 3.用 TestPublisher 手动发出元素
     *
     * 4.用 PublisherProbe 检查执行路径
     */

    /**
     * 调试方式一：开启调试模式
     * 使用Hooks.onOperatorDebug()在程序初始的地方开启调试模式
     */
    @Test
    public void testReactorDebug(){
        Hooks.onOperatorDebug();
        Flux.range(1, 3)
                .flatMap(n -> Mono.just(n + 100))
                .single()
                .map(n -> n * 2)
                .subscribe(System.out::println);
    }

    /**
     * 调试方式二：使用checkpoint操作符买点调试
     * 使用方案1开启全局调试有较高的成本即影响性能，我们可以在可能发生错误的代码中加入操作符 checkpoint 来检测本段响应式流的问题，而不影响其他数据流的执行。
     * checkpoint 通常用在明确的操作符的错误检查，类似于埋点检查的概念。同时该操作符支持 3个重载方法：
     * checkpoint();
     * checkpoint(String description);
     * checkpoint(String description, boolean forceStackTrace);
     * description 为埋点描述，forceStackTrace 为是否打印堆栈
     */

    @Test
    public void testReactorCheckPoint(){
        Flux.range(1, 3)
                .flatMap(n -> Mono.just(n + 100))
                .single()
                .map(n -> n * 2)
//                .checkpoint()
//                .checkpoint("发生异常")
//                .checkpoint("打印堆栈", true)
                .subscribe(System.out::println);
    }

    /**
     * 调试方式三：启用调试代理（todo 此种方式没有搞明白）
     * step1：项目中引入reactor-tools依赖
     * step2：使用ReactorDebugAgent.init()初始化代理。由于该代理是在加载类时对其进行检测，因此放置它的最佳位置是在main（String []）方法中的所有其他项之前
     * step3：如果是测试类，使用如下代码处理现有的类。注意，在测试类中需要提前运行，比如在 @Before 中
     */
    @BeforeEach
    public void before(){
        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
    }

    /**
     * 测试log
     * 除了基于 stack trace 的方式调试分析，我们还可以使用 log操作符，来跟踪响应式流并记录日志。
     * 将它添加到操作链上之后，它会读取每一个再其上游的 Flux 和 Mono 事件（包括 onNext、onError、onComplete、Subscribe、Cancel 和 Request）。
     */
    @Test
    public void testLog(){
        //尝试交换下take和log的顺序，看看有啥变化
        Flux.range(1, 10)
                .log()
                .take(3)
                .log()
                .subscribe();
    }
}
