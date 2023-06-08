package com.demo.practise.common.beanutils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapToObjectUtilsByIntros {

    /**
     * 使用Introspector转换
     * @param map
     * @param beanClass
     * @return Object
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IntrospectionException
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws IllegalAccessException, InstantiationException, IntrospectionException, InvocationTargetException {
        if (null == map){
            return null;
        }
        Object o = beanClass.newInstance();
        BeanInfo beanInfo = Introspector.getBeanInfo(o.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            Method setter = propertyDescriptor.getWriteMethod();
            if (setter != null) {
                setter.invoke(o, map.get(propertyDescriptor.getName()));
            }
        }
        return o;
    }

    public static Map<String, Object> objectToMap(Object obj) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        if (obj == null) {
            return null;
        }
        HashMap<String, Object> map = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String key = propertyDescriptor.getName();
            if (key.compareToIgnoreCase("class") == 0){
                continue;
            }
            Method getter = propertyDescriptor.getReadMethod();
            Object value  = getter != null ? getter.invoke(obj): null;
            map.put(key, value);
        }
        return map;
    }
}
