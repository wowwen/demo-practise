package com.demo.aop.unirespformate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.HashMap;

/**
 * 为了保持 API 的一致性和易用性，通常需要使用统一的数据返回格式。 一般而言，一个标准的数据返回格式应该包括以下几个元素：
 *
 * 状态码：用于标志请求成功失败的状态信息；
 * 消息：用来描述请求状态的具体信息；
 * 数据：包含请求的数据信息；
 * 时间戳：可以记录请求的时间信息，便于调试和监控。
 * 实现统一的数据返回格式可以使用 @ControllerAdvice + ResponseBodyAdvice 的方式实现，具体步骤如下：
 *
 * 创建一个类，并添加 @ControllerAdvice 注解；
 * 实现 ResponseBodyAdvice 接口，并重写 supports 和 beforeBodyWrite 方法。
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 此方法返回true，则执行下面的beforeBodyWrite方法，反之则不行
     * @param returnType
     * @param converterType
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "");
        result.put("data", body);
        if (body instanceof String){
            //需要对String进行特殊处理。 如果返回的 body 原始数据类型是 String ，则会出现类型转化异常，即 ClassCastException。
            try{
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
