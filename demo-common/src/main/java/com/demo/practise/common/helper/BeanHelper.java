package com.demo.practise.common.helper;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author owen
 * @date 2024/10/22 23:45
 * @description 对Bean的一些操作工具
 */
public class BeanHelper {

    /**
     * 获取字段类型为list里的实体类 例如获取 List<DictDto> dictDtos 中的DictDto类
     *
     * @param field
     * @return
     */
    public static Class<?> getListFieldType(Field field) {
        Type genericType = field.getGenericType();
        ParameterizedType pt = (ParameterizedType) genericType;
        Class cla = (Class) pt.getActualTypeArguments()[0];
        return cla;
    }

    /**
     * 判断是否是基础数据类型的包装类型
     *
     * @param clz
     * @return
     */
    public static boolean isWrapClass(Class clz) {
        try {
            return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }


    public static <T, M> T copyProperties(M source, Class<T> clazz) {
        if (Objects.isNull(source) || Objects.isNull(clazz)) {
            throw new IllegalArgumentException();
        }
        T t = null;
        try {
            t = clazz.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, t);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException |InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 列表对象属性复制
     *
     * @param sources 源对象列表
     * @param clazz   目标对象class
     * @param <T>     目标对象类型
     * @param <M>     源对象类型
     * @return 目标对象列表
     */
    public static <T, M> List<T> copyObjects(List<M> sources, Class<T> clazz) {
        if (Objects.isNull(sources) || Objects.isNull(clazz)) {
            throw new IllegalArgumentException();
        }
        return Optional.of(sources)
                .orElse(Lists.newArrayList())
                .stream().map(m -> copyProperties(m, clazz))
                .collect(Collectors.toList());
    }
}
