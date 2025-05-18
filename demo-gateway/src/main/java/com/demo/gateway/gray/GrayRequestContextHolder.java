package com.demo.gateway.gray;

import lombok.Data;
import org.springframework.http.HttpHeaders;

/**
 * @author owen
 * @date 2025/4/27 11:09
 * @description 上下文持有者
 * 设计考虑：
 * 线程安全：基于ThreadLocal实现，适合Reactor的线程模型
 *
 * 简洁API：提供静态方法便于全局访问
 *
 * 可扩展性：上下文对象可随需求增加字段
 */
public class GrayRequestContextHolder {
    // 使用ThreadLocal保证线程安全
    private static final ThreadLocal<GrayRequestContext> CONTEXT = new ThreadLocal<>();

    public static GrayRequestContext getContext() {
        return CONTEXT.get();
    }

    public static void setContext(GrayRequestContext context) {
        CONTEXT.set(context);
    }

    public static void clearContext() {
        CONTEXT.remove(); // 必须显式清除防止内存泄漏
    }
}


