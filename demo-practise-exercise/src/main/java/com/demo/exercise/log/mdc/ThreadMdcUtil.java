package com.demo.exercise.log.mdc;

import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 判断当前线程对应MDC的Map是否存在，存在则设置
 * 设置MDC中的traceId值，不存在则新生成，针对子线程的情况，如果是子线程,设置线程的traceId，MDC中traceId不为null
 */
public class ThreadMdcUtil {
    public static String TRACE_ID = "traceId";

    public static void setTraceIdIfAbsent(){
        if (MDC.get(TRACE_ID) == null){
            MDC.put(TRACE_ID, TraceIdUtil.getTraceId());
        }
    }

    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context){
        return () -> {
            if (context == null){
                MDC.clear();
            }
            else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            }finally {
                MDC.clear();
            }
        };
    }

    public static Runnable wrap(final Runnable runnable, final Map<String, String> context){
        return () -> {
            if (context == null){
                MDC.clear();
            }
            else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            }finally {
                MDC.clear();
            }
        };
    }

    /**
     * 上面的写法等同于以下写法
     * @return 重新返回的是包装后的Runnable,在该任务执行之前（即runnable.run()之前），先将主线程的Map设置到当前线程中（即MDC.setContextMap(context);）
     * 这样，子线程和主线程MDC对应的Map就是一样的了
     */
//    public static Runnable wrap(final Runnable runnable, final Map<String, String> context){
//        return new Runnable() {
//            @Override
//            public void run() {
//                if (context == null){
//                    MDC.clear();
//                }
//                else {
//                    MDC.setContextMap(context);
//                }
//                setTraceIdIfAbsent();
//                try {
//                    runnable.run();
//                }finally {
//                    MDC.clear();
//                }
//            }
//        };
//    }

    public static class TraceIdUtil{
        public static String getTraceId(){
            return UUID.randomUUID().toString().replace("-", "");
        }
    }
}
