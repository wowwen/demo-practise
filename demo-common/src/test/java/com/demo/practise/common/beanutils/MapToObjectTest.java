package com.demo.practise.common.beanutils;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MapToObjectTest.class)
public class MapToObjectTest {

    @Test
    public void testMapToObjectUtils1() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("=====1======");

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "张三");
        map.put("age", "10");

        User user = (User) MapToObjectUtilsByApache.mapToObject(map, User.class);
        System.out.println(user.getName());
        System.out.println(user.getAge());
    }

    @Test
    public void testObjectToMap1(){
        User user = new User("李四", "20");
        Map<String, String> map = (Map<String, String>) MapToObjectUtilsByApache.objectToMap(user);
        System.out.println(map.get("name"));
        System.out.println(map.get("age"));
    }

    @Test
    public void testMapToObjectUtils2(){
        System.out.println("====2======");
        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "王五");
        map.put("age", "30");

        User userBean = new User();
        User user = MapToObjectUtilsByCglib.mapToBean(map, userBean);
        System.out.println(user.getName());
        System.out.println(user.getAge());

        User user1 = User.builder()
                .name("赵六")
                .age("40")
                .build();
        Map<String, Object> userMap = MapToObjectUtilsByCglib.beanToMap(user);
        String name =(String) userMap.get("name");
        System.out.println(name);
    }


    @Data
    @Builder
    public static class User{
        public User(String name, String age) {
            this.name = name;
            this.age = age;
        }
        public User(){
        }
        String name;
        String age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
    }
}
