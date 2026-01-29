package com.demo.exercise.spel;

/**
 * @author jiangyw
 * @date 2026/1/28 10:16
 * @description
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Value注解使用SpEL
 */
@Component
public class ValueDemo {

    // 1. 字面量
    @Value("#{100}")
    private Integer literalValue;

    // 2. 系统属性
    @Value("#{systemProperties['user.home']}")
    private String userHome;

    // 3. 环境变量
    @Value("#{systemEnvironment['PATH']}")
    private String path;

    // 4. 配置文件属性
    @Value("${server.port:8080}")
    private Integer serverPort;

    // 5. Bean属性
    @Value("#{userService.userCount}")
    private Integer userCount;

    // 6. 表达式计算
    @Value("#{T(java.lang.Math).random() * 100}")
    private Double randomValue;

    // 7. 列表注入
    @Value("#{'${user.roles}'.split(',')}")
    private List<String> roles;

    // 8. 三元表达式
    @Value("#{${user.enabled:true} ? '启用' : '禁用'}")
    private String userStatus;

    // 9. Elvis运算符
    @Value("#{config.name ?: 'default'}")
    private String configName;

    public void printValues() {
        System.out.println("字面量: " + literalValue);
        System.out.println("用户主目录: " + userHome);
        System.out.println("随机值: " + randomValue);
        System.out.println("用户角色: " + roles);
        System.out.println("用户状态: " + userStatus);
    }
}

