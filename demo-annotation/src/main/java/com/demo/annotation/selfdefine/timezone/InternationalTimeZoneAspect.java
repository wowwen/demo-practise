package com.demo.annotation.selfdefine.timezone;

import cn.hutool.core.util.ObjectUtil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.demo.practise.common.helper.BeanHelper;
import com.demo.practise.common.helper.DateHelper;
import com.demo.practise.common.helper.ResponseHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jiangyw
 * @date 2024/10/21 14:59
 * @description 根据前端传进来的时区，转换成对应的时间返回
 */
@Slf4j
@Aspect
public class InternationalTimeZoneAspect {

    private static final String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 电离层预报查询接口是post请求 也要时间转换
     */
    private static final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_DEFAULT));
    private static final ThreadLocal<SimpleDateFormat> formatter1 = ThreadLocal.withInitial(() -> new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US));

    /**
     * 拦截所有Controller的方法
     */
    @Pointcut("execution(* com.demo.annotation..controller.*Controller.*(..))")
    public void controllerCut() {
        //吃方法作为切面，没有方法体

    }
    /**
     * 切入点增强处理
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("controllerCut()")
    public Object changeController(ProceedingJoinPoint joinPoint) throws Throwable {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        RequestAttributes requestAttributes = Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
        HttpServletRequest request = ((HttpServletRequest) Objects.requireNonNull(requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST)));
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取时区并设置格式化日期的时区
        String timeZone = request.getHeader("Time-Zone");
        if (ObjectUtil.isEmpty(timeZone)) {
            //设置默认时区
            timeZone = "UTC";
        }
        Object[] args = joinPoint.getArgs();
        //执行方法前时间参数转为当前服务器时区时间
        beforeMethod(timeZone, args, method);
        //执行方法
        Object result = joinPoint.proceed(args);
        formatter.get().setTimeZone(TimeZone.getTimeZone(timeZone));

        if (!(result instanceof ResponseEntity) || ObjectUtil.isEmpty(result)) {
            return result;
        }
        //获取返回类型
        Class<?> returnType = getReturnType("body", result);
        //返回类型为空或者是基础数据类型或其包装类型，不进行转换
        if (ObjectUtil.isEmpty(returnType) || Objects.requireNonNull(returnType).isPrimitive() || BeanHelper.isWrapClass(returnType)) {
            return result;
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(result);
        JSONObject body = jsonObject.getJSONObject("body");
        //返回数据的转换
        Object data = body.get("data");
        if (ObjectUtil.isNotEmpty(data)) {
            //数据转换
            dataReplace(returnType, data, timeZone);
            //封装为响应类型结果
            result = ResponseHelper.successful(data);
        }
        return result;
    }

    /**
     * 数据转换
     *
     * @param returnType
     * @param data
     * @param timeZone
     */
    public static void dataReplace(Class<?> returnType, Object data, String timeZone) {
        List<Field> fieldList = getChangeField(returnType);
        if (data instanceof JSONObject) {
            JSONObject dataJson = (JSONObject) JSON.toJSON(data);
            int size = dataJson.keySet().size();
            //对象中包含list(如分页查询返回的数据）分页查询
            if (ObjectUtil.isNotEmpty(dataJson.getJSONArray("items")) && ObjectUtil.isNotEmpty(dataJson.get(
                    "_meta")) && size == 2) {
                JSONArray list = dataJson.getJSONArray("items");
                for (Object object : list) {
                    filedReplace(object, timeZone, returnType, fieldList);
                }
            } else if (ObjectUtil.isNotEmpty(dataJson.getJSONArray("list")) && ObjectUtil.isNotEmpty(dataJson.get("totalCounts")) && size == 3){
                JSONArray list = dataJson.getJSONArray("list");
                for (Object object : list) {
                    filedReplace(object, timeZone, returnType, fieldList);
                }
            }else {
                filedReplace(dataJson, timeZone, returnType, fieldList);
            }
        } else if (data instanceof JSONArray) {
            //返回的是list类型的
            JSONArray jsonArray = (JSONArray) JSON.toJSON(data);
            for (Object object : jsonArray) {
                filedReplace(object, timeZone, returnType, fieldList);
            }
        }
    }

    /**
     * 字段值替换
     *
     * @param obj
     * @param timeZone
     * @param returnType
     * @param fields
     */
    public static void filedReplace(Object obj, String timeZone, Class<?> returnType, List<Field> fields) {
        //反射获取字段
        // JSONObject dataJson = (JSONObject) obj;
        JSONObject dataJson = (JSONObject) JSON.toJSON(obj);
        for (Field field : fields) {
            field.setAccessible(true);
            //判断子段上是否有@change注解
            String fieldName = field.getName();
            if (ObjectUtil.isNotEmpty(dataJson.get(fieldName))) {
                //字段为String类型直接替换
                if (field.getType().isAssignableFrom(String.class)) {
                    //处理String类型的时间
                    Object fieldValue = dataJson.get(fieldName);
                    String annotationType = field.getAnnotation(Change.class).type();
                    if ("time".equals(annotationType)) {
                        try {
                            Date date = null;
                            String time = fieldValue.toString();
                            //不是时间戳
                            if (!StringUtils.isNumeric(time)) {
                                //判断日期时间格式
                                if (DateHelper.isDateVail(time)) {
                                    date = new SimpleDateFormat(PATTERN_DEFAULT).parse(time);
                                    time = formatter.get().format(timeZoneReplace("UTC", date, timeZone, PATTERN_DEFAULT));
//                                    time = formatter.get().format(date);
                                } else {
                                    date = formatter1.get().parse(time);
//                                    time = formatter.get().format(date);
                                    time = formatter.get().format(timeZoneReplace("UTC", date, timeZone, PATTERN_DEFAULT));
                                }
                            }
                            dataJson.put(fieldName, time);
                        } catch (ParseException e) {
                            log.error("格式化时间出错: {}", e.getMessage(), e);
                        }
                    }
                } else if (field.getType().isAssignableFrom(List.class)) {
                    //字段类型为List，递归遍历list
                    JSONArray fieldArray = dataJson.getJSONArray(fieldName);
                    Class<?> filedClass = null;
                    filedClass = BeanHelper.getListFieldType(field);
                    List<Field> fieldList = getChangeField(filedClass);
                    for (Object object : fieldArray) {
                        JSONObject jsonObject = (JSONObject) object;
                        filedReplace(jsonObject, timeZone, filedClass, fieldList);
                    }
                } else if (field.getType().isAssignableFrom(Date.class)) {
                    //字段类型为Date，时间按时区进行转换
                    Object fieldValue = dataJson.get(fieldName);
                    try {
                        Object formatTime = timeZoneReplace("UTC", fieldValue, timeZone, PATTERN_DEFAULT);
                        // String format = formatter.get().format(fieldValue);
                        if (ObjectUtil.isNotEmpty(formatTime)) {
                            dataJson.put(fieldName, formatTime);
                        }
                    } catch (Exception parseException) {
                        log.error("格式化时间出错: {}", parseException.getMessage(), parseException);
                    }
                } else {
                    //字段类型为dto类对象
                    Class<?> type = field.getType();
                    List<Field> fieldList = getChangeField(type);
                    filedReplace(dataJson.get(fieldName), timeZone, type, fieldList);
                }
            }
        }
        formatter.remove();
        formatter1.remove();
    }

    /**
     * 获取返回结果的数据类型
     *
     * @param fieldName
     * @param o
     * @return
     */
    private Class<?> getReturnType(String fieldName, Object o) {
        try {
            if (ObjectUtil.isEmpty(o)) {
                return null;
            }
            //参数字段为list 但不是分页查询
            if (fieldName.equals("list") && !(o instanceof PageInfo)) {
                if (o instanceof List) {
                    List objectList = (List<Object>) o;
                    Object obj = objectList.get(0);
                    return obj.getClass();
                }
            }
            //字符串类型和基础数据类型直接返回
            if (o.getClass().equals(String.class) || o.getClass().isPrimitive() || BeanHelper.isWrapClass(o.getClass())) {
                return o.getClass();
            }
            JSONObject jsonObject = (JSONObject) JSON.toJSON(o);
            if (ObjectUtil.isNotEmpty(jsonObject.get(fieldName))) {
                return getSubReturnType(fieldName, o);
            }
            else {
                fieldName = "list";
                if (ObjectUtil.isNotEmpty(jsonObject.get(fieldName))){
                    return getSubReturnType(fieldName, o);
                }
                return o.getClass();
            }
        } catch (Exception e) {
            log.error("method invoke error", e);
            return null;
        }
    }


    private Class<?> getSubReturnType(String fieldName, Object o) throws Exception {
        String firstLetter = fieldName.substring(0, 1).toUpperCase();
        String getter = "get" + firstLetter + fieldName.substring(1);
        if (fieldName.equals("items")){
            Map o1 = (Map) o;
            Object o2 = o1.get("items");
            List objectList = (List<Object>) o2;
            Object obj = objectList.get(0);
            return obj.getClass();
        }

        Method method = o.getClass().getMethod(getter, new Class[]{});
        Object value = method.invoke(o, new Object[]{});
        if (fieldName.equals("body")) {
            return getReturnType("data", value);
        } else if (fieldName.equals("data")) {
            return getReturnType("items", value);
        } else if (fieldName.equals("list")) {
            List objectList = (List<Object>) value;
            Object obj = objectList.get(0);
            return obj.getClass();
        }
        return null;
    }

    /**
     * 执行方法前将时间参数转成服务器时间
     *
     * @param timeZone
     * @param params
     * @param method
     */
    public static void beforeMethod(String timeZone, Object[] params, Method method) {
        Class<?>[] paramsType = method.getParameterTypes();
        //获取方法参数上的注解（因为方法可以有多参数；参数上可以有多注解，返回二维数组）
        Annotation[][] an = method.getParameterAnnotations();
        int index = 0;
        //循环参数
        for (Annotation[] an1 : an) {
            Object param = params[index];
            //循环参数上的注解
            for (Annotation an2 : an1) {
                //有自定义校验注解
                if (an2 instanceof Time && ObjectUtils.isNotEmpty(param)) {
                    Class<?> classType = paramsType[index];
                    //时区转换
                    Object result = timeReplace(timeZone, param, classType);
                    if (ObjectUtils.isNotEmpty(result)) {
                        params[index] = result;
                    }
                }
            }
            index++;
        }
    }

    /**
     * 时间类的参数值替换成当前服务器时区的值
     *
     * @param timeZone
     * @param value
     * @param classType
     * @return
     */
    public static Object timeReplace(String timeZone, Object value, Class classType) {
        //时间参数是 String
        if (value instanceof String || value instanceof Date) {
            Object format = timeZoneReplace(timeZone, value, "UTC", PATTERN_DEFAULT);
            return format;
        } else {
            //获取该参数类型
            //反射所有字段
            List<Field> fields = getChangeField(classType);
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object o = field.get(value);
                    field.set(value, timeReplace(timeZone, o, field.getType()));
                } catch (IllegalAccessException e) {
                    log.error("获取或设置字段的值错误: {}", e.getMessage(), e);
                }
            }
        }
        return null;
    }

    /**
     * 将源时区的时间转成目标时区的时间
     *
     * @param sourceTimeZone
     * @param sourceTime
     * @param desTimeZone
     * @param formatPattern
     * @return
     */
    public static Object timeZoneReplace(String sourceTimeZone, Object sourceTime, String desTimeZone, String formatPattern) {
        Object result = sourceTime;
        try {
            String stringTime = sourceTime.toString();
            SimpleDateFormat currentSdf = new SimpleDateFormat(formatPattern);
            currentSdf.setTimeZone(TimeZone.getTimeZone(sourceTimeZone));
            SimpleDateFormat destSdf = new SimpleDateFormat(formatPattern);
            destSdf.setTimeZone(TimeZone.getTimeZone(desTimeZone));
            if (sourceTime instanceof String) {
                String time = sourceTime.toString();
                Date parse = currentSdf.parse(time);
                result = destSdf.format(parse);
            } else if (sourceTime instanceof Date) {
                String format = destSdf.format(sourceTime);
                result = currentSdf.parse(format);
            } else if (StringUtils.isNumeric(stringTime)) {
                //时间戳处理
                Date date = new Date(Long.parseLong(stringTime));
                String format = destSdf.format(date);
                result = currentSdf.parse(format);
            }
        } catch (Exception e) {
            log.error("格式化时间出错: {}", e.getMessage(), e);
            return sourceTime;
        }
        return result;
    }

    /**
     * @description: 获取类中带有自定义注解@change的字段
     * @author: liangsw
     * @date: 2022/10/13 16:30
     * @param: [cla]
     * @return: java.util.List<java.lang.reflect.Field>
     **/
    public static List<Field> getChangeField(Class<?> cla) {
        //获取返回类的所有字段
        List<Field> allFields = new ArrayList<>();
        Class<?> i = cla;
        while (i != null && i != Object.class) {
            Collections.addAll(allFields, i.getDeclaredFields());
            i = i.getSuperclass();
        }
        //筛选出字段中带有自定义注解的字段
        List<Field> fieldList = new ArrayList<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Change.class)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }
}
