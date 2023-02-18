package com.demo.exercise.log.mdc.http.httpclient;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author jiang
 * 在使用HTTP调用第三方服务接口时，traceId将丢失，需要对HTTP调用工具进行改造，在发送时在request中 header中添加traceId，在下层被调用方中添加拦截器获取header中的traceId添加到MDC中。
 * 比较常用额HTTP调用方式有HttpClient，OKHttp，RestTemplate，以下为这几种的解决方案
 */

/**
 * 方法1：实现HttpClient拦截器
 */
public class HttpClientTraceIdInterceptor implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest httpRequest, HttpContext httpContext) throws HttpException, IOException {
        String traceId = MDC.get("traceId");
        //如果当前线程
        if (!StringUtils.isEmpty(traceId)){
            httpRequest.addHeader("traceId", traceId);
        }
    }

    /**
     * 通过addInterceptorFirst方法为HttpClient添加拦截器
     */
    private static CloseableHttpClient httpClient = HttpClientBuilder.create()
            .addInterceptorFirst(new HttpClientTraceIdInterceptor())
            .build();
}
