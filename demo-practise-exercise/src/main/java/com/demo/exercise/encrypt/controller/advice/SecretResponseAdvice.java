package com.demo.exercise.encrypt.controller.advice;

import com.demo.exercise.encrypt.filter.SecretFilter;
import com.demo.practise.common.helper.EncryptUtils;
import com.demo.practise.common.helper.Md5Helper;
import com.demo.practise.common.resp.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 响应加密
 */
@ControllerAdvice
public class SecretResponseAdvice implements ResponseBodyAdvice {
    private Logger logger = LoggerFactory.getLogger(SecretResponseAdvice.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //判断是否需要加密
        Boolean respFlag = SecretFilter.secretThreadLocal.get();
        String secretKey = SecretFilter.clientPrivateKeyThreadLocal.get();
        //清除本地缓存
        SecretFilter.secretThreadLocal.remove();
        SecretFilter.clientPrivateKeyThreadLocal.remove();

        if (null != respFlag && respFlag){
            if (body instanceof Message){
                //可以直接加密整个响应，也可以分属性加密具体的value
                //这里采用分属性加密具体的value
                // 外层加密级异常
                /**
                 * 也可以当错误时，不加密错误信息
                 *  // 外层加密级异常
                 *                 if (SECRET_API_ERROR == ((ResponseBasic) o).getCode()) {
                 *                     return SecretResponseBasic.fail(((ResponseBasic) o).getCode(), ((ResponseBasic) o).getData(), ((ResponseBasic) o).getMsg());
                 *                 }
                 */
                try {
                    // 使用FastJson序列号会导致和之前的接口响应参数不一致，后面会重点讲到
                    //
                    String data = EncryptUtils.aesEncrypt(objectMapper.writeValueAsString(body), secretKey);
                    // 增加签名
                    long timestamp = System.currentTimeMillis() / 1000;
                    int salt = EncryptUtils.genSalt();
                    String dataNew = timestamp + "" + salt + "" + data + secretKey;
                    String newSignature = Md5Helper.genSignature(dataNew);
                    return new Message<>(200, "success", data, newSignature, timestamp, salt);
                } catch (Exception e) {
                    e.printStackTrace();
                    return new Message<>(400, "failed", "" );
                }
            }
        }
        return body;
    }
}
