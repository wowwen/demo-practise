package com.demo.practise.common.beanutils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import java.util.Map;

/**
 * 采用alibaba-fastjson转换，效率较低
 * 思路：先将bean转为json，再将json转成map
 */
public class MapToObjectUtilsByFastJson {

    /**
     * @param map
     * @param beanClass
     * @return
     * @throws Exception
     */
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        String jsonStr = JSONObject.toJSONString(map);
        return JSONObject.parseObject(jsonStr, beanClass);
    }

    /**
     * 
     * @param obj
     * @return
     */
    public static Map<String, Object> objectToMap(Object obj) {
        String jsonStr = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(jsonStr);
        //或者如下指定转换后的类型
        //return JSONObject.parseObject(jsonStr, new TypeReference<Map<String, Object>>() {});
    }
}
