package com.demo.netty.proxy.constant;

import io.netty.util.AttributeKey;

/**
 * @author owen
 * @date 2024/9/21 22:56
 * @description
 */
public interface Constants {

    interface UserChannelAttributeKey{
        AttributeKey<Integer> USER_ID = AttributeKey.newInstance("userId");
        AttributeKey<String> USER_NAME = AttributeKey.newInstance("username");
        AttributeKey<String> USER_LOGIN_CONTENT = AttributeKey.newInstance("userLoginContent");
        AttributeKey<String> REAL_SERVER_HOST = AttributeKey.newInstance("realServerHost");
        AttributeKey<String> REAL_SERVER_PORT = AttributeKey.newInstance("realServerPort");
        AttributeKey<Long> LOGIN_TIME = AttributeKey.newInstance("logintime");
    }
}
