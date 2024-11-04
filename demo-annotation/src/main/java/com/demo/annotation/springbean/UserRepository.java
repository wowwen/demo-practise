package com.demo.annotation.springbean;

import org.springframework.stereotype.Repository;

/**
 * @author jans9
 * @description  @Repository 注解也是@Component注解的延伸，与@Component注解一样，被此注解标注的类会被Spring自动管理起来，@Repository注解用于标注DAO层的数据持久化类
 */
@Repository
public class UserRepository {
}
