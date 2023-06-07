package com.demo.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Arrays;
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

    /**
     * Flux#collectMap
     * 将Flux发出的所有元素按照键生成器和值生成器收集到Map中，之后由Mono发出。
     * Flux提供了3个重载方法：它们的主要区别在于是否提供值生成器和初始的Map，意同Java Stream中的Collectors#toMap
     * <K>    Mono<Map<K, T>> collectMap(Function<? super T, ? extends K> keyExtractor)
     * <K, V> Mono<Map<K, V>> collectMap(Function<? super T, ? extends K> keyExtractor, Function<? super T, ? extends V> valueExtractor)
     * <K, V> Mono<Map<K, V>> collectMap(Function<? super T, ? extends K> keyExtractor, Function<? super T, ? extends V> valueExtractor, Supplier<Map<K, V>> mapSupplier)
     */
    @Test
    public void testCollectMap(){
        Flux.just(1,2,3,4,5,3,1)
                .collectMap(n -> n, n -> n + 100)
                .subscribe(System.out::println);
        //输出{1=101, 2=102, 3=103, 4=104, 5=105}
    }

    /**
     * Flux#collectMultimap
     * collectMultimap与collectMap的区别在于，map中的value类型不同。collectMultimap中是集合，collectMap是元素
     * collectMultimap对于流中出现重复的key的value，加入到集合中，而collectMap做了替换。在这点上，reactor不如Java Stream中的Collectors#toMap方法，没有提供key重复时的合并函数。
     * Flux的collectMultimap也提供了3个重载方法
     * <K>    Mono<Map<K, Collection<T>>> collectMultimap(Function<? super T, ? extends K> keyExtractor)
     * <K, V> Mono<Map<K, Collection<V>>> collectMultimap(Function<? super T, ? extends K> keyExtractor, Function<? super T, ? extends V> valueExtractor)
     * <K, V> Mono<Map<K, Collection<V>>> collectMultimap(Function<? super T, ? extends K> keyExtractor, Function<? super T, ? extends V> valueExtractor, Supplier<Map<K, Collection<V>>> mapSupplier)
     */
    @Test
    public void testCollectMultimap(){
        Flux.just(1,2,3,4,5,3,1)
                .collectMultimap(n -> n, n-> n+100)
                .subscribe(System.out::println);
        //{1=[101, 101], 2=[102], 3=[103, 103], 4=[104], 5=[105]}
    }

    /**
     * Flux#collectSortedList
     * 将Flux发出的元素在完成时进行排序，之后由Mono发出
     * Flux提供了2个重载方法:
     * Mono<List<T>> collectSortedList()
     * Mono<List<T>> collectSortedList(@Nullable Comparator<? super T> comparator) --提供了比较器
     */
    @Test
    public void testCollectSortedList(){
        Flux.just(1,3,5,3,2,5,1,4)
                .collectSortedList()
                .subscribe(System.out::println);//[1, 1, 2, 3, 3, 4, 5, 5]
    }

    /**
     * Flux#concatMap
     * 将响应式流中元素按顺序转换为目标类型的响应式流，之后再将这些流连接起来。该方法提供了2个重载方法，传递的第2个参数为内部生成响应式流的预取数量。
     */
    @Test
    public void testConcatMap(){
        Flux.range(3, 8)
                .concatMap(n -> Flux.just(n - 10, n, n + 10), 3)
                .subscribe(System.out::println);
    }

    /**
     * Flux#concatMapDelayError
     * concatMapDelayError和concatMap区别在于，当内部生成响应式流发出error时，是否延迟响应error。
     * 该方法提供了3个重载方法，支持传递参数：是否延迟发出错误和预取数量
     */
    @Test
    public void testConcatMapDelayError(){
        Flux.range(3, 8)
                .concatMapDelayError(n -> {
                    if (n == 4){
                        return Flux.error(new NullPointerException());
                    }
                    return Flux.just(n -10, n , n + 10);
                }).subscribe(System.out::println, System.err::println);

    }

    /**
     * Flux#concatIterable
     * concatIterable和concatMap的区别在于内部返回的类型不同， 一个为Iterable， 一个为响应式流
     */
    @Test
    public void testConcatIterable(){
        Flux.range(3, 8)
                .publishOn(Schedulers.single())
                .concatMapIterable(n -> {
                    if (n == 4){
                        throw new NullPointerException();
                    }
                    return Arrays.asList(n -10 , n, n + 10);
                })
                .onErrorContinue((e, n) -> System.err.println("数据：" + n + "发生错误：" + e))
                .subscribe(System.out::println);
    }

    /**
     * elapsed
     * 收集响应式流中元素的间隔发出时间，转换为时间间隔和旧元素组测的Tuple2的响应流
     */
    @Test
    public void testElapsed() throws InterruptedException {
        Flux.interval(Duration.ofMillis(300))
                .take(20)
                .elapsed(Schedulers.parallel())
                .subscribe(System.out::println);
        Thread.sleep(7000);
    }

    /**
     * expand
     * 从上层节点逐层展开方式递归展开树形节点
     */
    @Test
    public void testExpand(){
        Flux.just(16, 18, 20)
                .expand(n -> {
                    if (n % 2 == 0){
                        return Flux.just(n / 2);
                    }else{
                        return Flux.empty();
                    }
                })
                .subscribe(System.out::println);
    }

    /**
     * expandDeep
     * 从上层节点逐个展开方式递归展开树形节点。expand与expandDeep的区别在于展开方式不同。另外它俩都提供了capacityHint指定递归时初始化容器的容量
     */
    @Test
    public void testExpandDeep(){
        Flux.just(16, 18, 20)
                .expandDeep(n -> {
                    if (n % 2 == 0){
                        return Flux.just(n / 2);
                    }else {
                        return Flux.empty();
                    }
                })
                .subscribe(System.out::println);
    }
}
