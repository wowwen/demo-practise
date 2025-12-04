package com.demo.netty.proxy.entity;

import io.netty.channel.Channel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author owen
 * @date 2024/9/21 4:07
 * @description 客户端发送的消息，可能是登录信息，也可能是位置信息
 */
@Data
public class ClientMsg implements Serializable {
    private static final long serialVersionUID = 5404315552277748435L;
    /**
     * 是否登录请求
     */
    private boolean loginAuth = false;
    /**
     * 发起客户端
     */
    private String userAgent;

    /**
     * 登录时间
     */
    private Long logintime;

    /**
     * 报文体
     */
    private String content;

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户通道
     */
    private Channel channel;
    /**
     * 登录真正服务器的session
     */
    private Long sessionId;

    /**
     * 断言，网格坐标
     */
    private String predicates;
}
