package com.demo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;

import java.time.ZonedDateTime;

/**
 * @author owen
 * @date 2025/3/17 23:38
 * @description
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.demo.gateway.**"})
@ConfigurationPropertiesScan("com.demo.gateway.*")
public class DemoGateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoGateWayApplication.class, args);
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("===gateway启动=====" + now);

    }

}
