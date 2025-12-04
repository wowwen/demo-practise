package com.demo.netty.proxy.config;

import lombok.Data;

import java.util.List;

/**
 * @author owen
 * @date 2024/8/25 18:05
 * @description
 */
@Data
public class NettyClient {
    /**
     * 终端连接到本proxy服务的端口
     */
    private Integer port;

    /**
     * 原项目中，这里放的是连接到C++服务的节点信息
     */
//    private List<ProxyRealServerNode> casters;
}
