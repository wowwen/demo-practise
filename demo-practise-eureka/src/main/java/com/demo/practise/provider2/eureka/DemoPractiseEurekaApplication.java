package com.demo.practise.provider2.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class DemoPractiseEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoPractiseEurekaApplication.class, args);
    }

}
