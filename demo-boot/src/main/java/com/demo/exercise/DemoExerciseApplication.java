package com.demo.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author owen
 * @date 2022/3/29 20:09
 * @description
 */
@SpringBootApplication
//@EnableJpaRepositories(basePackages = "com.demo.exercise.jpa") //当需要指定repository包时候可以通过此注解，这里不需要，springboot会扫描本包及子包
public class DemoExerciseApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoExerciseApplication.class, args);
    }
}
