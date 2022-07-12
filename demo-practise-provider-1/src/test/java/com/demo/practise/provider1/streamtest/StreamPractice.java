package com.demo.practise.provider1.streamtest;

import lombok.Data;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

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
    public void filter(){
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
    public void trans(){
        List<Person> ps = new ArrayList<Person>(){
            {
                Person p1 = new Person();
                p1.setAge(11);
                p1.setName("张强");

                Person p2 = new Person();
                p2.setAge(17);
                p2.setName("李四");

                Person p3 = new Person();
                p3.setAge(20);
                p3.setName("jack");

                add(p1);
                add(p2);
                add(p3);
            }
        };
        //取出所有age字段为一个List
        List<Integer> allAge  = ps.stream().map(Person::getAge).collect(Collectors.toList());
        System.out.println(allAge);
        //取出年龄最大的
        Integer maxAge  = allAge.stream().max(Integer::compareTo).get();
        //取出年龄最小的
        Integer minAge = allAge.stream().min(Integer::compareTo).get();

        System.out.println(maxAge);
        System.out.println(minAge);
    }

    @Data
    class Person{
        private String name;
        private Integer age;

        private Date joinDate;
        private String label;
    }
}
