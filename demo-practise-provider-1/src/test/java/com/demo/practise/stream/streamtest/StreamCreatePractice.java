package com.demo.practise.stream.streamtest;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.*;

/**
 * ©Copyright ©1968-2020 Midea Group,ITf
 *
 * @FileName: StreamPractice
 * @Author: jiangyw8
 * @Date: 2020-10-8 22:17
 * @Description: cnblogs.com/funnyzpc/p/10382053.html
 * <p>
 * Stream是一类用于替代对集合操作的工具类+Lambda式编程，他可以替代现有的遍历、过滤、求和、求最值、排序、转换等
 * 流是惰性的，源数据上的计算仅在终端操作启动时执行，源元素仅在需要时才被使用。
 */
@Slf4j
public class StreamCreatePractice {

    @Test
    public void stream() {
        //操作list
        List<Map<String, String>> mapList = new ArrayList() {
            {
                Map<String, String> m = new HashMap();
                m.put("a", "1");
                Map<String, String> m2 = new HashMap<>();
                m2.put("b", "2");
                add(m);
                add(m2);
            }
        };
        mapList.stream().forEach(item -> {
            System.out.println(item);
        });

        //操作Map
        Map<String, Object> mp = new HashMap() {
            {
                put("a", "1");
                put("b", "2");
                put("c", "3");
                put("d", "4");
            }
        };
        mp.keySet().stream().forEachOrdered(item -> {
            System.out.println(mp.get(item));
        });
    }


    /**
     * 过滤filter
     */
    @Test
    public void filter() {
        List<Integer> list = new ArrayList() {
            {
                add(1);
                add(10);
                add(12);
                add(33);
                add(44);
            }
        };
        List<Integer> integerList = list.stream().filter(item -> {
            return item > 30;
        }).collect(Collectors.toList());
        System.out.println(list);
        System.out.println(integerList);
    }

    /**
     * 转换map,极值,平均值，分组
     */
    @Test
    public void trans() {
        List<Person> personList = new ArrayList<Person>() {
            {
                Person p1 = new Person();
                p1.setAge(11);
                p1.setName("张强");
                p1.setSex("男");

                Person p2 = new Person();
                p2.setAge(17);
                p2.setName("李四");
                p2.setSex("女");

                Person p3 = new Person();
                p3.setAge(20);
                p3.setName("jack");
                p3.setSex("男");

                add(p1);
                add(p2);
                add(p3);
            }
        };
        //取出所有age字段为一个List
        List<Integer> allAge = personList.stream().map(Person::getAge).collect(Collectors.toList());
        System.out.println(allAge);

        //取出年龄最大的, 写法1
        Integer maxAge = allAge.stream().max(Integer::compareTo).get();
        //取出年龄最大的，写法2
        Optional<Person> max = personList.stream().max((p1, p2) -> p1.getAge() - p2.getAge());
        if (max.isPresent()) {
            Integer age = max.get().getAge();
        }
        //取出年龄最大的，写法3
        Optional<Person> max1 = personList.stream().max(Comparator.comparingInt(Person::getAge));
        if (max1.isPresent()) {
            Integer age = max1.get().getAge();
        }

        //取出年龄最小的, 写法1
        Integer minAge = allAge.stream().min(Integer::compareTo).get();
        //取出年龄最小的， 写法2
        Optional<Person> min = personList.stream().collect(Collectors.minBy((p1, p2) -> p1.getAge() - p2.getAge()));
        if (min.isPresent()) {
            Integer age = min.get().getAge();
        }
        //取出年龄最小的，写法3
        Optional<Person> min1 = personList.stream().min(Comparator.comparingInt(Person::getAge));
        if (min1.isPresent()) {
            Integer age = min1.get().getAge();
        }

        System.out.println(maxAge);
        System.out.println(minAge);

        //求平均值
        Double avgAge = personList.stream().collect(Collectors.averagingInt(Person::getAge));
        System.out.println("平均年龄为：" + avgAge);

        //求和，写法1
        Integer reduce = personList.stream().map(Person::getAge).reduce(0, Integer::sum);
        //求和，写法2
        int sum = personList.stream().mapToInt(Person::getAge).sum();

        //统计数量
        long count = personList.stream().count();

        //简单分组
        //按照具体年龄分组
        Map<Integer, List<Person>> collect = personList.stream().collect(Collectors.groupingBy(Person::getAge));
        //根据性别分组
        Map<Integer, List<Person>> sexGroup = personList.stream().collect(Collectors.groupingBy(p -> {
            if (p.getSex().equals("男")) {
                return 1;
            } else {
                return 0;
            }
        }));
        //多级分组
        //1.先根据年龄分组
        //2.再根据性别分组
        Map<Integer, Map<String, Map<Integer, List<Person>>>> multiGroup = personList.stream().collect(Collectors.groupingBy(Person::getAge, Collectors.groupingBy(Person::getSex, Collectors.groupingBy(p -> {
            if (p.getSex().equals("男")) {
                return 1;
            } else {
                return 0;
            }
        }))));

        //转换成其他集合/数组
        //转成HashSet
        HashSet<Person> personHashSet = personList.stream().collect(Collectors.toCollection(HashSet::new));
        //转成Set
        Set<Person> personSet = personList.stream().collect(Collectors.toSet());
        //转成ArrayList
        ArrayList<Person> personArrayList = personList.stream().collect(Collectors.toCollection(ArrayList::new));
        //转成Object[]数组
        Object[] objects = personList.stream().toArray();
        //转换成Person[]对象数组
        Person[] people = personList.stream().toArray(Person[]::new);
    }

    @Data
    class Person {
        private String name;
        private Integer age;
        private String sex;
        private Date joinDate;
        private String label;
    }

    @Test
    public void diffStreamParallelStream(){
        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9);
        numberList.stream().forEach(n -> System.out.println(String.format("Stream The Current Thread's ID is %d and output number %d ",Thread.currentThread().getId(), n)));
        System.out.println("stream是有序的");
        numberList.parallelStream().forEach(n -> System.out.println(String.format("ParallelStream The Current Thread's ID is %d and output number %d ",Thread.currentThread().getId(), n)));
        System.out.println("parallelStream()是并行无序的");
        numberList.parallelStream().forEachOrdered(n -> System.out.println(String.format("ParallelStream forEach Ordered The Current Thread's ID is %d and output number %d ",Thread.currentThread().getId(), n)));
        System.out.println("parallelStream()并行无序变并行有序需要用forEachOrdered");
        System.out.println("系统一共有"+Runtime.getRuntime().availableProcessors()+"个cpu");
    }

    /**
     * 比较各循环花费时间
     * 结论：
     * for循环花费时间：9083
     * forEach循环花费时间:9091
     * stream执行花费时间：9172
     * parallelStream花费时间：2031
     * parallelStream order花费时间：9080
     * @throws InterruptedException
     */
    @Test
    public void speedStreamParallelStream() throws InterruptedException {
        List<Integer> numberList = Arrays.asList(1,2,3,4,5,6,7,8,9);
        long forBegin = System.currentTimeMillis();
        for (int i = 0; i < numberList.size(); i++) {
            Thread.sleep(1000);
        }
        System.out.println(String.format("for循环花费时间：%d", System.currentTimeMillis() - forBegin));

        long beginning = System.currentTimeMillis();
        for (Integer integer : numberList) {
            Thread.sleep(1000);
        }
        System.out.println(String.format("forEach循环花费时间:%d", System.currentTimeMillis() - beginning));

        long streamBegin = System.currentTimeMillis();
        numberList.stream().forEach(n -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(String.format("stream执行花费时间：%d", System.currentTimeMillis() - streamBegin));

        long parallelStreamBegin = System.currentTimeMillis();
        numberList.parallelStream().forEach(n -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(String.format("parallelStream花费时间：%d", System.currentTimeMillis() - parallelStreamBegin));

        long parallelStreamOrderBegin = System.currentTimeMillis();
        numberList.parallelStream().forEachOrdered(n -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(String.format("parallelStream order花费时间：%d", System.currentTimeMillis() - parallelStreamOrderBegin));
    }

    /**
     * 由说法1w一下for最快，10w级stream最快，实测没有这结论，bullshit！
     * @throws InterruptedException
     */
    @Test
    public void streamEfficiency() throws InterruptedException {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < 5000; i++) {
            linkedList.add(i);
        }
        long forIBegin = System.currentTimeMillis();
        for (int i = 0; i < linkedList.size(); i++) {
            Thread.sleep(1);
        }
        System.out.println(String.format("for循环花费时间：%d", System.currentTimeMillis() - forIBegin));

        long forEachBegin = System.currentTimeMillis();
        for (Integer num : linkedList) {
            Thread.sleep(1);
        }
        System.out.println(String.format("forEach循环花费时间：%d", System.currentTimeMillis() - forEachBegin));

        long streamBegin = System.currentTimeMillis();
        linkedList.stream().forEach(n -> {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(String.format("stream执行花费时间：%d", System.currentTimeMillis() - streamBegin));

    }


    /**
     * Stream的分类
     * 序号	类名	                说明
     * 1	BaseStream	    Stream接口的父接口
     * 2	Stream	        泛型类型的Stream
     * 3	IntStream	    整形Stream
     * 4	LongStream	    长整型Stream
     * 5	DoubleStream	浮点型Stream
     */

    /**
     * 用集合创建Stream
     */
    @Test
    public void testCreateStream() {
        {
            /**
             * 集合创建Stream
             */
            Collection collection = new ArrayList<Integer>();
            collection.add(1);
            collection.add(2);
            collection.add(3);

            //返回此collection作为数据源的Stream
            Stream stream = collection.stream();
            //返回此collection作为数据源的可能并行的Stream
            Stream parallelStream = collection.parallelStream();
        }

        /**
         * 数组创建Stream
         */
        String[] strArray    = {"a", "b", "c"};
        int[] intArray       = {1, 2, 3};
        double[] doubleArray = {1, 2, 3};
        //创建数组Stream
        Stream<String> strStream  = Arrays.stream(strArray);
        IntStream      intStream  = Arrays.stream(intArray);
        DoubleStream doubleStream = Arrays.stream(doubleArray);
        IntStream     intStream1  = Arrays.stream(intArray, 1, 2);
        log.info("字符串数组中字符数：{}", strStream.mapToInt(String::length).sum());
        // stream 只能使用一次，如下报错
        // log.info("整形数组数量：{}, 求和：{}", intStream.count(), intStream.sum());
        log.info("整形数组数量：{}, 求和：{}", Arrays.stream(intArray).count(), intStream.sum());
        log.info("整形数组2求和：{}", intStream1.sum());
        log.info("浮点型数组求和：{}", doubleStream.sum());

        /**
         * 值创建Stream
         */
        //构建Integer类型额Stream
        IntStream intStream2 = IntStream.of(14, 2, 3, 41);
        log.info("值创建Stream中一串数中最大值：{}", intStream2.distinct().max().getAsInt());

        //构建String类型额Stream
        Stream<String> stringStream = Stream.of("aaa", "bbbaaa");
        log.info("字符串中出现的字母数(去重)：{}", stringStream
                // 去除非字母，转换为小写，分隔为单个字符
                .flatMap(str -> Stream.of(
                        str.replaceAll("[^\\p{Alpha}]", "")
                                .toLowerCase()
                                .split("")))
                .distinct()
                .count());

        /**
         * 函数方式创建Stream
         */
        AtomicInteger index = new AtomicInteger(0);
        //方式1：使用generate函数创建一个新的无限无序Stream流， RandomUtils中都是伪随机数
        Stream<Integer> generateStream = Stream.generate(RandomUtils::nextInt);
        int num = generateStream
                .mapToInt(n -> {
                    log.info("数组[{}]的值为：{}", index.getAndIncrement(), n);
                    return n;
                }).limit(50).max().getAsInt();
        log.info("一串随机数组中最大值：{}", num);

        //方式2：使用iterate方式创建一个新的无限有序Stream流
        IntStream iterateStream = IntStream.iterate(1, n -> n + 1);
        log.info("1-500的和为：{}", iterateStream.limit(500).sum());

        //创建空的顺序流
        Stream<Object> empty = Stream.empty();
        log.info("空的顺序流的元素个数为：{}", empty.count());
        //使用两个Stream创建组合Stream
        IntStream concat = IntStream.concat(intStream1, intStream2);
        IntSummaryStatistics statistics = concat.summaryStatistics();
        log.info("组合整形数据流中元数个数：{}\n总和：{}\n最大值：{}\n最小值：{}\n平均值：{}",
                statistics.getCount(), statistics.getSum(), statistics.getMax(), statistics.getMin(), statistics.getAverage());

        //创建begin至end逐渐加1的整型Stream
        IntStream range = IntStream.range(1, 501);
        //使用建造者模式创建Stream
        Stream.Builder<Object> builder = Stream.builder();
        builder.add(RandomUtils.nextInt())
                .add(RandomUtils.nextInt());
        Stream<Object> build = builder.build();
        log.info("构建了{}个元素", build.peek(System.out::println).count());

        {
            /**
             *  使用StreamSupport方式创建Stream
             */
            double[] doubles = {1.0, 2.0, 3.0, 4.0};
            //创建一个Spliterator.OfDouble类型的对象
            Spliterator.OfDouble spliterator = Spliterators.spliterator(doubles, Spliterator.ORDERED);
            //使用StreamSupport.doubleStream()方法创建一个DoubleStream流
            DoubleStream streamSpt = StreamSupport.doubleStream(spliterator, false);
            streamSpt.forEach(System.out::print);
            //这个示例只是演示了如何使用StreamSupport.doubleStream()方法，实际上，这个方法可以接受各种类型的Spliterator对象，并且可以用于创建各种类型的流，比如IntStream和LongStream
        }
    }

}
