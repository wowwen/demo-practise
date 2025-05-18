package com.demo.gateway.gray;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 基于自定义规则的灰度发布
 * @author owen
 * @date 2025/4/26 16:07
 * @description 自定义负载均衡规则，创建自定义的ReactorServiceInstanceLoadBalancer
 * 关键点说明：
 * 1.ReactorServiceInstanceLoadBalancer接口：Spring Cloud提供的响应式负载均衡器接口
 *
 * 2.ObjectProvider：延迟获取实例列表提供者，避免启动时依赖问题
 *
 * 3.choose方法：核心方法，完成实例选择流程
 *
 * 4.灰度上下文传递：通过GrayRequestContextHolder获取请求级别的灰度信息
 */
public class GrayReleaseLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private final String serviceId;
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final GrayReleaseRule grayReleaseRule;

    public GrayReleaseLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                                   String serviceId,
                                   GrayReleaseRule grayReleaseRule) {
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.grayReleaseRule = grayReleaseRule;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier  = serviceInstanceListSupplierProvider.getIfAvailable();
        return  supplier.get(request).next().map(instances -> getInstanceResponse(instances, request));
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        if (instances.isEmpty()) {
            // 无可用实例时返回空响应
            return new EmptyResponse();
        }

        // 获取灰度请求上下文（从ThreadLocal中）
        GrayRequestContext context = GrayRequestContextHolder.getContext();

        // 应用灰度规则选择实例
        ServiceInstance instance = grayReleaseRule.choose(instances, context);
        // 返回选中的实例
        return new DefaultResponse(instance);
    }
}
