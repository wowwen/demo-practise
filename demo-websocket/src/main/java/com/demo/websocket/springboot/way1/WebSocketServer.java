package com.demo.websocket.springboot.way1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 如果是做一个客户端，则注解为@ClientEndpoint
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */

/**
 * @author jans9
 * 由于使用的是内嵌容器，而内嵌容器需要被Spring管理并初始化，所以需要给WebSocketServer加上@Component这么一个注解。
 * websocket连接时调用这里的路径进行连接
 */
@Component
@Service
@ServerEndpoint("/websocket/{sid}")
public class WebSocketServer {
    private static final Logger log = LoggerFactory.getLogger(WebSocketServer.class);

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    //接收sid
    private String sid = "";

    /**
     * websocket连接建立调用的方法
     *
     * @param session
     * @param sid
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        this.session = session;
        webSocketSet.add(this);
        this.sid = sid;
        addOnlineCount();
        try {
            sendMessage("连接成功");
            log.info("新窗口开始监听：{}，当前总数：{}", sid, getOnlineCount());
        } catch (Exception e) {
            log.error("开启websocket异常", e);
        }
    }

    /**
     * websocket连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        minusOnlineCount();
        log.info("断开sid为：{}的websocket连接.现有的websocket连接数：{}", sid, getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到sid:{}信息：{}", sid, message);
        //群发消息
        for (WebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            }catch (IOException e){
                log.error("群发消息异常：", e);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("发生错误：{}", throwable);
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    /**
     * 推送消息到自定义窗口
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) {
        log.info("推送消息：{}到：{}窗口", message, sid);
        for (WebSocketServer item : webSocketSet) {
            try {
                if (null == sid) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (Exception e) {
                log.error("消息未找到匹配sid，继续");
                continue;
            }
        }
    }

    public static synchronized  int getOnlineCount(){
        return onlineCount;
    }
    public static synchronized void addOnlineCount(){
        WebSocketServer.onlineCount++;
    }
    public static synchronized void minusOnlineCount(){
        WebSocketServer.onlineCount--;
    }
    public static CopyOnWriteArraySet<WebSocketServer> getWebSocketSet(){
        return webSocketSet;
    }

}
