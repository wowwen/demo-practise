package com.demo.websocket.springboot.way3;

import com.demo.websocket.springboot.way2.ClientUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jiangyw
 * @date 2024/7/15 16:27
 * @description
 */
@Slf4j
public class WebSocketServiceImpl implements WebSocketService{

    private final Map<Integer, WebSocketSession> clients = new ConcurrentHashMap<>();

//    @Override
//    public void handleOpen(WebSocketSession session) {
//        //在建立websocket时存储用户信息
//        Map<String, Object> attributes = session.getAttributes();
//        ClientUserInfo clientUserInfo = (ClientUserInfo) attributes.get("clientUserInfo");
//        clients.put(clientUserInfo.getId(), session);
//        log.info("新用户已链接，当前连接数：{}", clients.size());
//    }

    /**
     * 解决websocket高并发时候的问题：The remote endpoint was in state [xxxx] which is an invalid state for call
     * 用Spring的ConcurrentWebSocketSessionDecorator将session再包一层
     * @param session 会话信息
     */
    @Override
    public void handleOpen(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        ClientUserInfo userInfo = (ClientUserInfo) attributes.get("clientUserInfo");
        clients.put(userInfo.getId(), new ConcurrentWebSocketSessionDecorator(session, 10 * 1000, 64000));
        log.info("新用户已链接，当前连接数：{}", clients.size());
    }

    @Override
    public void handleClose(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        ClientUserInfo clientUserInfo = (ClientUserInfo) attributes.get("clientUserInfo");
        clients.remove(clientUserInfo.getId());
        log.info("websocket链接已删除，当前连接数：{}", clients.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, String message) {
        Map<String, Object> attributes = session.getAttributes();
        ClientUserInfo clientUserInfo = (ClientUserInfo) attributes.get("clientUserInfo");
        //处理客户端发送过来的消息
        log.info("收到客户端：{}消息：{}", clientUserInfo.getId(), message);
    }

    @Override
    public void sendMessage(WebSocketSession session, String message) {
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(WebSocketSession session, TextMessage textMessage) {
        try {
            session.sendMessage(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendMessage(Integer userId, String message) {
        this.sendMessage(userId, new TextMessage(message));
    }

    @Override
    public void sendMessage(Integer userId, TextMessage textMessage) {
        WebSocketSession webSocketSession = clients.get(userId);
        if (webSocketSession.isOpen()){
            try {
                webSocketSession.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void broadcast(String message) {
        clients.values().forEach(session -> {
            if (session.isOpen()){
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                   throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void broadcast(TextMessage textMessage) {
        clients.values().forEach(session -> {
            if (session.isOpen()){
                try {
                    session.sendMessage(textMessage);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 解决高并发时候session的状态不一致的问题的方式1-上锁
     * 缺点 ：当并发度较高时，越后面排队等待锁的人被block的越久。
     * @param message 消息
     */
    public void broadcastV2(String message){
        clients.values().forEach(session -> {
            if (session.isOpen()){
                synchronized (session){
                    try{
                        session.sendMessage(new TextMessage(message));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public void handleError(WebSocketSession session, Throwable ex) {
        log.error("websocket异常：{}, session id：{}", ex, session.getId());
    }
}
