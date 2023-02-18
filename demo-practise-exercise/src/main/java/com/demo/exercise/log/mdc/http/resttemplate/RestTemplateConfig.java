package com.demo.exercise.log.mdc.http.resttemplate;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        //为RestTemplate设置拦截器
        restTemplate.setInterceptors(Collections.singletonList(new RestTemplateTraeIdInterceptor()));
        return restTemplate;
    }
}
