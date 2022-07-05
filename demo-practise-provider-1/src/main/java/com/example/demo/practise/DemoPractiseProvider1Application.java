package com.example.demo.practise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DemoPractiseProvider1Application {

    public static void main(String[] args) {
        SpringApplication.run(DemoPractiseProvider1Application.class, args);
    }

}
