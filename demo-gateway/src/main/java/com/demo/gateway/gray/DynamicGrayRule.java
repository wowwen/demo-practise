package com.demo.gateway.gray;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @date 2025/4/27 11:19
 * @description 动态规则实现
 * 动态规则特点：
 * 多条件支持：Header、用户白名单、随机比例等多种灰度条件
 *
 * 热更新：通过@RefreshScope支持配置动态生效
 *
 * 灵活组合：多种规则可以组合使用
 */
@Component
@RequiredArgsConstructor
public class DynamicGrayRule implements GrayReleaseRule {
    private final GrayReleaseProperties properties;

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances, GrayRequestContext context) {
        if (!properties.isEnabled()) {
            return getDefaultInstance(instances); // 灰度未启用
        }

        // 1. 检查Header规则
        for (Map.Entry<String, String> entry : properties.getHeaderRules().entrySet()) {
            if (context.getHeaders().containsKey(entry.getKey()) &&
                    context.getHeaders().getFirst(entry.getKey()).equals(entry.getValue())) {
                return getGrayInstance(instances); // 命中Header规则
            }
        }

        // 2. 检查灰度用户
        if (context.getUserId() != null && properties.getGrayUsers().contains(context.getUserId())) {
            return getGrayInstance(instances);
        }

        // 3. 按比例随机灰度
        if (Math.random() < properties.getDefaultGrayRatio()) {
            return getGrayInstance(instances);
        }

        return getDefaultInstance(instances);
    }

    // 辅助方法：获取灰度实例
    private ServiceInstance getGrayInstance(List<ServiceInstance> instances) {
        return instances.stream()
                .filter(inst -> "v2".equals(inst.getMetadata().get("version")))
                .findFirst()
                .orElse(instances.get(0));
    }

    // 辅助方法：获取默认实例
    private ServiceInstance getDefaultInstance(List<ServiceInstance> instances) {
        return instances.stream()
                .filter(inst -> "v1".equals(inst.getMetadata().get("version")))
                .findFirst()
                .orElse(instances.get(0));
    }
}