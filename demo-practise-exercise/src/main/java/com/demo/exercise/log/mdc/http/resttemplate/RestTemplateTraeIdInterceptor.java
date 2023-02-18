package com.demo.exercise.log.mdc.http.resttemplate;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RestTemplateTraeIdInterceptor implements ClientHttpRequestInterceptor {
    @Autowired
    RestTemplate restTemplate;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        String traceId = MDC.get("traceId");
        if (null != traceId){
            request.getHeaders().add("traceId", traceId);
        }
        return execution.execute(request, body);
    }

    /**
     * 为RestTemplate添加拦截器
     */

}
