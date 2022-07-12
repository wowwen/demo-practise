package com.demo.practise.common.arraycastlist;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: ArrayCastList
 * @Author: jiangyw8
 * @Date: 2020-10-26 9:24
 * @Description: 数组转List的方式对比
 */
@RunWith(SpringRunner.class)
public class ArrayCastList {

    /**
     * 方式一：不能对List增删，只能查改
     * 通过Arrays.asList(strArray) 方式,将数组转换List后，不能对List增删，只能查改，否则抛异常：java.lang.UnsupportedOperationException
     * 原因解析：Arrays.asList(strArray)返回值是java.util.Arrays类中一个私有静态内部类java.util.Arrays.ArrayList，它并非java.util.ArrayList类。
     * java.util.Arrays.ArrayList类具有 set()，get()，contains()等方法，但是不具有添加add()或删除remove()方法,所以调用add()方法会报错。
     *
     * 使用场景：Arrays.asList(strArray)方式仅能用在将数组转换为List后，不需要增删其中的值，仅作为数据源读取使用。
     */

    @Test
    public void testArrayCast2ListError(){
        String[] strArray = new String[2];
        List<String> strList = Arrays.asList(strArray);
        //对转换后的List插入一条数据
        strList.add("str1");
        System.out.println(strList);
    }

    /**
     * 方式二、数组转为List后，支持增删改查的方式
     * 通过ArrayList的构造器，将Arrays.asList(strArray)的返回值由java.util.Arrays.ArrayList转为java.util.ArrayList。
     *
     * 使用场景：需要在将数组转换为List后，对List进行增删改查操作，在List的数据量不大的情况下，可以使用。
     *
     * 结果[null, null, str1]
     */
    @Test
    public void testArrayCast2ListRight(){
        String[] strArray = new String[2];
        ArrayList<String> list = new ArrayList<>(Arrays.asList(strArray));
        list.add("str1");
        System.out.println(list);
    }

    /**
     * 方式三：通过集合工具类Collections.addAll()方法(最高效)
     *
     * 通过Collections.addAll(arrayList, strArray)方式转换，根据数组的长度创建一个长度相同的List，然后通过Collections.addAll()方法，将数组中的元素转为二进制，然后添加到List中，这是最高效的方法。
     *
     * 使用场景：需要在将数组转换为List后，对List进行增删改查操作，在List的数据量巨大的情况下，优先使用，可以提高操作速度。
     *
     * 结果：[null, null, str1]
     */
    @Test
    public void testArrayCast2ListEfficient(){
        String[] strArray = new String[2];
        ArrayList<String> arrayList = new ArrayList<>(strArray.length);
        Collections.addAll(arrayList, strArray);
        arrayList.add("str1");
        System.out.println(arrayList);
    }

    /**
     * 如果JDK版本在1.8以上，可以使用流stream来将下列3种数组快速转为List，分别是int[]、long[]、double[]，其他数据类型比如short[]、byte[]、char[]，在JDK1.8中暂不支持。
     * 由于这只是一种常用方法的封装，不再纳入一种崭新的数组转List方式，暂时算是java流送给我们的常用工具方法吧
     */
    @Test
    public void testArrayCast2ListJdk8(){
        List<Integer> intList= Arrays.stream(new int[] { 1, 2, 3, }).boxed().collect(Collectors.toList());
        List<Long> longList= Arrays.stream(new long[] { 1, 2, 3 }).boxed().collect(Collectors.toList());
        List<Double> doubleList= Arrays.stream(new double[] { 1, 2, 3 }).boxed().collect(Collectors.toList());

        String[] arrays = {"tom", "jack", "kate"};
        List<String> stringList = Stream.of(arrays).collect(Collectors.toList());

        System.out.println(intList);
        System.out.println(longList);
        System.out.println(doubleList);
        System.out.println(stringList);
    }


    @Test
    public void addNullTest() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(null, null);
        hashMap.put(null, "Hello");
        hashMap.put("Hello", null);
        System.out.println(hashMap.get(null));
        System.out.println(hashMap.get("Hello"));
    }

    @Test
    public void list2String(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list.toString().replace("[","").replace("]", ""));
    }

    @Test
    public void lastDays(){
        LocalDate now = LocalDate.now();
        LocalDate localDate = now.minusDays(1);
        LocalDate localDate1 = now.minusDays(7);
        LocalDate localDate2 = now.minusWeeks(1);
        System.out.println(now);
        System.out.println(localDate);
        System.out.println(localDate1);
        System.out.println(localDate2);
    }


    public static Map<String, List<Map<String, Object>>> transition(List<Map<String, Object>> list){
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        // 分组
        for(Map<String, Object> temp : list) {
            // 获取Map的每一对值
            Iterator<Map.Entry<String, Object>> iterator = temp.entrySet().iterator();
            while (iterator.hasNext()) {
                List<Map<String, Object>> listAndMap = new ArrayList<>();
                // 获取到每一个实体
                Map.Entry<String, Object> entity = iterator.next();
                if (map.containsKey(entity.getKey())) {
                    // 获取原来存在的数据
                    List<Map<String, Object>> lm = map.get(entity.getKey());
                    lm.add(new HashMap<String, Object>() {{
                        put(entity.getKey(), entity.getValue());
                    }});
                    Collections.sort(lm, (param1, param2) ->
                            (param2.get(entity.getKey()).toString().compareTo(param1.get(entity.getKey()).toString()))
                    );
                    map.replace(entity.getKey(), lm);
                } else {
                    listAndMap.add(new HashMap<String, Object>() {{
                        put(entity.getKey(), entity.getValue());
                    }});
                    map.put(entity.getKey(), listAndMap);
                }
            }
        }
        return map;
    }



    @Test
    public  void main(){
        Date date = new Date();
        String nowTime = String.valueOf(date.getTime() / 1000);
        int dayNum = (int) ((Integer.valueOf(nowTime) - 1) / 3600 / 24);
        System.out.println(dayNum);
    }
}
