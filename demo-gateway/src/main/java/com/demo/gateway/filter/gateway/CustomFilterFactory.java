package com.demo.gateway.filter.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author owen
 * @date 2025/4/24 15:16
 * @description
 */
@Component
@Slf4j
public class CustomFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Resource
    CustomFilter customFilter;

    @Override
    public GatewayFilter apply(Object config) {
        System.out.println("hhhhhhhh");
        return customFilter;
    }
}
