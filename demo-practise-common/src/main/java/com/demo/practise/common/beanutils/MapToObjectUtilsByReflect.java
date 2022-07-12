package com.demo.practise.common.beanutils;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 思路：通过Java反射，获取bean类的属性和值，在转换到map所对应的键值对中，此方法比转json串效率高一点 
 */
public class MapToObjectUtilsByReflect {

    /**
     * 利用reflect转换
     * map转java对象
     * @param map
     * @param beanClass
     * @return Object
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws IllegalAccessException, InstantiationException {
        if (map == null) {
            return null;
        }
        Object o = beanClass.newInstance();
        while (null != beanClass.getSuperclass()){
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers)){
                    continue;
                }
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                field.set(o, map.get(field.getName()));
                // 恢复访问控制权限
                field.setAccessible(accessFlag);
            }
        }
        return o;
    }

    /**
     * Object转换成Map
     * @param object 待转换对象
     * @return Map<String, Object></>
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object object) throws IllegalAccessException {
        if (object == null) {
            return null;
        }
        Class<?> beanClass = object.getClass();
        Map<String, Object> map = new HashMap<>();

        while (null != beanClass.getSuperclass()){
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                Object value = field.get(object);
                // 恢复访问控制权限
                field.setAccessible(accessFlag);

                if (value != null && !StringUtils.isEmpty(value.toString())) {
                    //如果是List,将List转换为json字符串
                    if (value instanceof List) {
                        value = JSON.toJSONString(value);
                    }
                    map.put(name, value);
                }
            }
            beanClass = beanClass.getSuperclass();
        }
        return map;
    }
}
