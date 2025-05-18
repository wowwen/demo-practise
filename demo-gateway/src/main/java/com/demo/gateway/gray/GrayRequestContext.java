package com.demo.gateway.gray;

import lombok.Data;
import org.springframework.http.HttpHeaders;

/**
 * @author owen
 * @date 2025/4/27 11:10
 * @description
 */
@Data // Lombok注解，自动生成getter/setter
public class GrayRequestContext {
    private HttpHeaders headers; // 原始请求头
    private String userId;      // 用户标识
    // 可扩展添加其他灰度决策字段
}