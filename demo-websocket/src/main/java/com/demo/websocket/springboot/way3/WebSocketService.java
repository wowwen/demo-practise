package com.demo.websocket.springboot.way3;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author jiangyw
 * @date 2024/7/15 13:40
 * @description
 */
public interface WebSocketService {

    /**
     * 处理会话连接
     * @param session 会话信息
     */
    void handleOpen(WebSocketSession session);

    /**
     * 处理会话关闭
     * @param session 会话信息
     */
    void handleClose(WebSocketSession session);

    /**
     * 处理消息
     * @param session 会话信息
     * @param message 消息
     */
    void handleMessage(WebSocketSession session, String message);

    /**
     * 发送消息
     * @param session 会话信息
     * @param message 消息
     */
    void sendMessage(WebSocketSession session, String message);

    /**
     * 发送消息
     * @param session 会话信息
     * @param textMessage 文本消息
     */
    void sendMessage(WebSocketSession session, TextMessage textMessage);

    /**
     * 点对点发送消息
     * @param userId 用户ID
     * @param message 字符串消息
     */
    void sendMessage(Integer userId, String message);

    /**
     * 点对点发送消息
     * @param userId 用户ID
     * @param textMessage 消息
     */
    void sendMessage(Integer userId, TextMessage textMessage);

    /**
     * 广播
     * @param message 字符串消息
     */
    void broadcast(String message);

    /**
     * 广播
     * @param textMessage 文本消息
     */
    void broadcast(TextMessage textMessage);

    /**
     * 处理会话异常
     * @param session 会话信息
     * @param ex 异常
     */
    void handleError(WebSocketSession session, Throwable ex);
}
