package com.demo.netty.proxy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author owen
 * @date 2024/8/25 16:57
 * @description 根据配置信息设置netty
 */
@Data
@ConfigurationProperties(prefix = "netty")
@Component
public class ClientNode {

    private List<NettyClient> clients;
}
