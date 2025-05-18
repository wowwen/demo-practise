package com.demo.gateway.gray;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author owen
 * @date 2025/4/27 9:16
 * @description 灰度规则接口
 */
public interface GrayReleaseRule {
    /**
     * 从可用实例中选择一个目标实例
     * @param instances 可用实例列表
     * @param context 灰度请求上下文
     * @return 选择的服务实例
     */
    ServiceInstance choose(List<ServiceInstance> instances, GrayRequestContext context);
}

/**
 * 基于Header的灰度规则
 * 规则实现要点：
 * 版本标识：通过实例元数据中的version字段区分不同版本
 *
 * 降级处理：当找不到符合版本要求的实例时返回第一个可用实例
 *
 * 可扩展性：可以轻松添加其他规则（如权重、用户ID等）
 * @author jans9
 */
@Component
class HeaderBasedGrayRule implements GrayReleaseRule {

    @Override
    public ServiceInstance choose(List<ServiceInstance> instances, GrayRequestContext context) {
        // 检查请求头中是否有灰度标记
        if (context.getHeaders().containsKey("X-Gray-Release") &&
                "true".equals(context.getHeaders().getFirst("X-Gray-Release"))) {
            // 返回v2版本实例
            return instances.stream()
                    .filter(inst -> "v2".equals(inst.getMetadata().get("version")))
                    .findFirst()
                    // 降级处理
                    .orElse(instances.get(0));
        }
        // 默认返回v1版本实例
        return instances.stream()
                .filter(inst -> "v1".equals(inst.getMetadata().get("version")))
                .findFirst()
                // 降级处理
                .orElse(instances.get(0));
    }
}


