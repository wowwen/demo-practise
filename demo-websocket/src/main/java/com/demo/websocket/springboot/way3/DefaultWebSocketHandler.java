package com.demo.websocket.springboot.way3;

import org.springframework.web.socket.*;

import javax.annotation.Resource;

/**
 * @author jiangyw
 * @date 2024/7/15 18:21
 * @description
 */
public class DefaultWebSocketHandler implements WebSocketHandler {
    @Resource
    private WebSocketService webSocketService;

    /**
     * 建立链接
     * @param session
     * @throws Exception
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        webSocketService.handleOpen(session);
    }

    /**
     * 处理接收到的消息
     * @param session 会话信息
     * @param message 消息
     * @throws Exception
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage){
            TextMessage textMessage = (TextMessage) message;
            webSocketService.handleMessage(session, textMessage.getPayload());
        }
    }

    /**
     * 处理异常
     * @param session 会话信息
     * @param exception 异常
     * @throws Exception
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        webSocketService.handleError(session, exception);
    }

    /**
     * 关闭链接
     * @param session 会话信息
     * @param closeStatus
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        webSocketService.handleClose(session);
    }

    /**
     * 是否支持发送部分消息
     * @return false
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
