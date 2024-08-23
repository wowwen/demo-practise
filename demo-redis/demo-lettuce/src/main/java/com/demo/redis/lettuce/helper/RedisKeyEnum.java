package com.demo.redis.lettuce.helper;

import lombok.Getter;

/**
 * @author jiangyw
 * @date 2024/8/7 19:53
 * @description 所有Redis的key的枚举类
 */
@Getter
public enum RedisKeyEnum {
    BIT_TEST("bit_test"),
    BIT_FIELD_TEST("bit_field_test"),
    HYPER_LOG_TEST("hyper_log_test"),
    ;

    private final String key;

    RedisKeyEnum(String key) {
        this.key = key;
    }
}
