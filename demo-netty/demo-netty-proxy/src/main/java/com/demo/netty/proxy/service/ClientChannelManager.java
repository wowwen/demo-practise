package com.demo.netty.proxy.service;

import com.demo.netty.proxy.entity.ClientMsg;
import io.netty.util.internal.StringUtil;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author owen
 * @date 2024/9/23 1:03
 * @description 关
 */
@Data
public class  ClientChannelManager {

   private static Map<String, ClientMsg> cache = new ConcurrentHashMap<>(1024);

    public static void add(String userName, ClientMsg msg) {
        if (!StringUtil.isNullOrEmpty(userName)) {
            cache.put(userName, msg);
        }
    }

    public static void remove(String userName) {
        if (!StringUtil.isNullOrEmpty(userName)) {
            cache.remove(userName);
        }
    }

    public static ClientMsg get(String userName) {
        if (!StringUtil.isNullOrEmpty(userName)) {
            return cache.get(userName);
        }
        return null;
    }


    public static void shutDownAll() {
        cache.forEach((k, v) -> v.getChannel().close());
    }
}
