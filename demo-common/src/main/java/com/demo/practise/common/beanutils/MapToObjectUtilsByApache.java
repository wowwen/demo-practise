package com.demo.practise.common.beanutils;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MapToObjectUtilsByApache {

    /**
     * map转java对象
     * 使用org.apache.commons.beanutils进行转换
     * 依赖的包为：
     * <dependency>
     * <groupId>commons-beanutils</groupId>
     * <artifactId>commons-beanutils</artifactId>
     * <version>1.9.4</version>
     * </dependency>
     * https://blog.csdn.net/liuyunyihao/article/details/86597002
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        if (null == map) {
            return null;
        }
        Object object = beanClass.newInstance();
        BeanUtils.populate(object, map);
        return object;
    }

    /**
     * java对象转Map
     *
     * @param object
     * @return Map
     */
    public static Map<?, ?> objectToMap(Object object) {
        if (null == object) {
            return null;
        }
        return new BeanMap(object);
    }
}
