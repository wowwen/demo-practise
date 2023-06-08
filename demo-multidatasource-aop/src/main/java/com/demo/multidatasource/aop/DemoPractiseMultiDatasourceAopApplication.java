package com.demo.multidatasource.aop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan(basePackages = "com.demo.multidatasource.**.mapper")
public class DemoPractiseMultiDatasourceAopApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoPractiseMultiDatasourceAopApplication.class, args);
    }
}
