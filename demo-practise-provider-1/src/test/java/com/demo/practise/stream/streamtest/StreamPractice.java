package com.demo.practise.stream.streamtest;

import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: StreamPractice
 * @Author: jiangyw8
 * @Date: 2020-10-8 22:17
 * @Description: cnblogs.com/funnyzpc/p/10382053.html
 * <p>
 * Stream是一类用于替代对集合操作的工具类+Lambda式编程，他可以替代现有的遍历、过滤、求和、求最值、排序、转换等
 */
public class StreamPractice {

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
     * 转换map和极值
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

    /**
     * Stream的分类
     * 序号	类名	        说明
     * 1	BaseStream	Stream接口的父接口
     * 2	Stream	    泛型类型的Stream
     * 3	IntStream	整形Stream
     * 4	LongStream	长整型Stream
     * 5	DoubleStream	浮点型Stream
     */

    /**
     * 用集合创建Stream
     */
    public void collectionStream() {
        Collection collection = new ArrayList<Integer>();
        collection.add(1);
        collection.add(2);
        collection.add(3);

        //返回此collection作为数据源的Stream
        Stream stream = collection.stream();
        //返回此collection作为数据源的可能并行的Stream
        Stream parallelStream = collection.parallelStream();


    }
}
