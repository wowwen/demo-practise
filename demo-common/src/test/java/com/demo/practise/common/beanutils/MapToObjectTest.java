package com.demo.practise.common.beanutils;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = MapToObjectTest.class) springboot升级成2.6.3后，已经由junit4迁移到JUnit Jupiter（JUnit
// 5），所以这两个注解不需要了。同时项目中只应存在Junit4或者JUnit Jupiter，同时存在可能导致冲突
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
