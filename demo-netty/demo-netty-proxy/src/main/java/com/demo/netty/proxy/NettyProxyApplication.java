package com.demo.netty.proxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author owen
 * @date 2024/8/25 16:12
 * @description netty代理启动类
 */
@SpringBootApplication(scanBasePackages = {"com.demo"}) //扫描以com.demo开头的包，主要是依赖了aop和lettuce模块，这两个模块的包名不同
//扫描数据库层mapper文件
@MapperScan(basePackages = {"com.demo.netty.proxy.mapper"})
public class NettyProxyApplication {
    public static void main(String[] args) {
        SpringApplication.run(NettyProxyApplication.class, args);
    }
}
