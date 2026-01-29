package com.demo.exercise.encrypt.controller.advice;

import com.alibaba.fastjson.JSON;
import com.demo.exercise.encrypt.bean.SecretReq;
import com.demo.exercise.encrypt.exception.ResultException;
import com.demo.exercise.encrypt.filter.SecretFilter;
import com.demo.exercise.encrypt.http.SecretHttpMessage;
import com.demo.practise.common.helper.EncryptUtils;
import com.demo.practise.common.helper.Md5Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Objects;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class SecretRequestAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return false;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        String httpBody;
        if (Boolean.TRUE.equals(SecretFilter.secretThreadLocal.get())) {
            //如果支持加密消息，进行消息解密
            httpBody = decryptBody(inputMessage);
        }else{
            //不解密
            httpBody = StreamUtils.copyToString(inputMessage.getBody(), Charset.defaultCharset());
        }
        return new SecretHttpMessage(new ByteArrayInputStream(httpBody.getBytes()), inputMessage.getHeaders());
    }

    private String decryptBody(HttpInputMessage inputMessage) throws IOException {
        InputStream body = inputMessage.getBody();
        String requestBody = StreamUtils.copyToString(body, Charset.defaultCharset());
        //验证签名
        HttpHeaders headers = inputMessage.getHeaders();
        if (CollectionUtils.isEmpty(headers.get("clientType"))
                || CollectionUtils.isEmpty(headers.get("timestamp"))
                || CollectionUtils.isEmpty(headers.get("salt"))
                || CollectionUtils.isEmpty(headers.get("signature"))) {
            throw new ResultException(4005, "请求解密参数错误，clientType、timestamp、salt、signature等参数传递是否正确传递");
        }
        String timestamp = String.valueOf(Objects.requireNonNull(headers.get("timestamp")).get(0));
        String salt = String.valueOf(Objects.requireNonNull(headers.get("salt")).get(0));
        String signature = String.valueOf(Objects.requireNonNull(headers.get("signature")).get(0));
        String privateKey = SecretFilter.clientPrivateKeyThreadLocal.get();
        SecretReq secretReq = JSON.parseObject(requestBody, SecretReq.class);
        String data = secretReq.getData();
        String newSign = "";
        if (!StringUtils.isEmpty(privateKey)) {
            newSign = Md5Helper.genSignature(timestamp + salt + data + privateKey);
        }
        if (!newSign.equals(signature)){
            throw new ResultException(406, "签名验证错误");
        }
        //解密
        try {
            String plainStr = EncryptUtils.aesDecrypt(data, privateKey);
            if (StringUtils.isEmpty(plainStr)){
                return "{}";
            }
            return plainStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new ResultException(407, "解密失败");
    }
}
