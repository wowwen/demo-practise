package com.demo.gateway.gray;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author owen
 * @date 2025/4/27 10:22
 * @description 负载均衡配置
 * 配置关键点：
 * 服务名称获取：从环境变量中读取当前路由对应的服务名
 *
 * 延迟加载：使用getLazyProvider避免循环依赖
 *
 * 规则注入：将灰度规则实现注入到负载均衡器中
 */
@Configuration
public class GrayLoadBalancerConfiguration {
    @Bean
    public ReactorLoadBalancer<ServiceInstance> grayReleaseLoadBalancer(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory, GrayReleaseRule grayReleaseRule) {
        // 获取服务名称（从路由配置的lb://service-name中提取）
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        // 创建自定义灰度负载均衡器
        return new GrayReleaseLoadBalancer(
                loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class),
                name,
                grayReleaseRule);
    }
}
