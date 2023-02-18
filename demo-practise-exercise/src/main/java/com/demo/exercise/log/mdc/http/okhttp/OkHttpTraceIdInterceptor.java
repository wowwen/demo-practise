package com.demo.exercise.log.mdc.http.okhttp;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.MDC;

import java.io.IOException;

public class OkHttpTraceIdInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        String traceId = MDC.get("traceId");
        Request request = null;
        if (null != traceId){
            //以原来的request新建一个请求，在其中添加请求体
            Request.Builder builder = chain.request().newBuilder();
            request = builder.addHeader("traceId", traceId).build();

        }
        //处理请求
        Response originResp = chain.proceed(request);
        return originResp;
    }

    /**
     * 为OkHttp添加拦截器
     */
    private static OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new OkHttpTraceIdInterceptor()).build();
}
