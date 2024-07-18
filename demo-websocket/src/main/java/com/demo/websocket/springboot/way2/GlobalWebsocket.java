package com.demo.websocket.springboot.way2;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author jiangyw
 * @date 2024/7/13 19:20
 * @description
 */
@Slf4j
@ServerEndpoint("/globalWs/{token}")
@Component
public class GlobalWebsocket extends SecureWebSocket{
    /**
     * key: userKye
     * value: GlobalWebsocket  这里你直接存储 session 也是可以的
     */
    private static final Map<String, GlobalWebsocket> CLIENTS = new ConcurrentHashMap<>();

    /**
     * // 如果允许 一个账号 多人登录的话  就 加上  "-" + tokenTime，因为每次登录的token过期时间都是不一样的
     * clientUserInfo.getId() + "-" + clientUserInfo.getAccount() ;
     */
    private String userKye;

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        if (!isTokenValid(token, true)) {
            sendAuthFailed(session);
            return;
        }

        this.session = session;
        this.userKye = clientUserInfo.getId() + "-" + clientUserInfo.getAccount() + "-" + super.tokenExpiresAt;
        CLIENTS.put(userKye, this);
        log.info("当前在线用户:{}", CLIENTS.keySet());

        try {
            session.getBasicRemote().sendText("连接成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public String onMessage(Session session, String message) {
        // 先判断当前token 是否已经到期了
        if (!isTokenValid(token, false)) {
            sendAuthFailed(session);
            return null;
        }

        try {
            session.getBasicRemote().sendText("received");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("ws session 发生错误:{}", throwable.getMessage());
    }

    @OnClose
    public void onClose(Session session) {
        CLIENTS.remove(userKye);
        log.info("ws 用户 userKey {} 已下线,当前在线用户:{}", userKye, CLIENTS.keySet());
    }

    /**
     * 发送消息
     *
     * @param messageVo
     */
    public void sendMessage(MessageVo messageVo) {
        try {
            this.session.getBasicRemote().sendText(JSON.toJSONString(messageVo));
        } catch (IOException e) {
            log.error("发送消息异常", e);
        }
    }

    /**
     * 向user精确用户发送消息
     *
     * @param userKey   由 account + "-" + refreshToken的签发时间组成，例："admin-1635830649000"
     * @param messageVo 消息内容
     */
    public static void sendToUser(String userKey, MessageVo messageVo) {
        GlobalWebsocket globalWebsocket = CLIENTS.get(userKey);
        if (null != globalWebsocket) {
            globalWebsocket.sendMessage(messageVo);
            return;
        }
        log.error("发送消息到指定用户,但是用户不存在,userKey is {},message is {}", userKey, JSON.toJSONString(messageVo));
    }

    /**
     * 全体组播消息
     * v1.0 此方法这里没有上锁，面对高并发的时候，可能会出现问题。因为高并发的情况下，出现了session抢占的问题，导致session状态不一致
     * java.lang.IllegalStateException: 远程 endpoint 处于 [xxxxxx] 状态，如： The remote endpoint was in state [TEXT_FULL_WRITING] which is an invalid state for call
     * @param
     */
    public static void broadcastV1(MessageVo messageVo) {
        CLIENTS.values().forEach(c -> {
                    Session curSession = c.session;
                    if (curSession.isOpen()) {
                        try {
                            curSession.getBasicRemote().sendText(JSON.toJSONString(messageVo));
                        } catch (IOException e) {
                            log.error("发送ws数据错误:{}", e.getMessage());
                        }
                    }
                }
        );
    }

    public static final ExecutorService WEBSOCKET_POOL_EXECUTOR = new ThreadPoolExecutor(
            20, 20,
            Integer.MAX_VALUE, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder()
                    .setNameFormat("GlobalWebsocket-executor-" + "%d")
                    .setUncaughtExceptionHandler((thread, throwable) -> log.error("ThreadPool {} got exception", thread, throwable)).build(),
            new ThreadPoolExecutor.AbortPolicy());

    public static void broadcastV2(MessageVo messageVo){
        CLIENTS.values().forEach(c -> {
            Session curSession = c.session;
            if (curSession.isOpen()){
                //建议一个session一个线程，避免一个session会话网络不好，会出现超时异常，当前线程会因此中断。导致后面的session没有进行发送操作。使用单个线程，单个session
                // 情况下避免session之间的相互影响
                WEBSOCKET_POOL_EXECUTOR.execute(() -> {
                    synchronized (curSession){
                        //双重锁检查，外边的isOpen第一遍过滤，里面加锁后第二遍过滤
                        if (curSession.isOpen()){
                            try {
                                curSession.getBasicRemote().sendText(JSON.toJSONString(messageVo));
                            } catch (IOException e) {
                                log.error("发送WS数据错误：{}", e.getMessage());
                            }
                        }
                    }
                });
            }

        });
    }
}
