package com.demo.exercise.log.mdc.simple;

import com.demo.exercise.log.mdc.ThreadMdcUtil;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 简单实用traceId
 * 但是有些情况下会有traceId获取不到
 * 1.子线程中打印日志丢失traceId
 * 2.HTTP调用丢失traceId
 */
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //如果上游调用有traceId就用上游的traceId
        String traceId = request.getHeader("traceId");
        if (StringUtils.isEmpty(traceId)){
            //如果上游没有traceId，则生成自己的
            traceId = ThreadMdcUtil.TraceIdUtil.getTraceId();
        }
        MDC.put("traceId", traceId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //调用结束后清空MDC
        MDC.remove("traceId");
    }
    /**
     * 修改配置文件的日志格式
     * 重点是%X{traceId}，traceId和MDC中的键名称一致
     * <property name="pattern">[TRACEID:%X{traceId}] %d{HH:mm:ss.SSS} %-5level %class{-1}.%M()/%L - %msg%xEx%n</property>
     */
}
