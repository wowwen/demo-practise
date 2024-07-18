package com.demo.websocket.springboot.way2;

import com.demo.practise.common.helper.SpringContextUtils;
import com.demo.websocket.springboot.common.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.io.IOException;

/**
 * @author jiangyw
 * @date 2024/7/13 15:54
 * @description
 */
@Slf4j
public abstract class SecureWebSocket {
    private static final ClientUserInfoService clientUserInfoService;

    static {
        clientUserInfoService = SpringContextUtils.getBean(ClientUserInfoService.class);
    }

    protected Session session;

    protected String token;

    protected Long tokenExpiresAt;

    protected ClientUserInfo clientUserInfo;

    /**
     * 验证token是否有效（包含有效期）
     *
     * @param token  token
     * @param isInit 是否对token和userInfo进行初始化赋值
     * @return boolean
     */
    protected boolean isTokenValid(String token, boolean isInit) {
        ClientUserInfo clientUserInfo;
        try {
            clientUserInfo = JwtUtils.getClientUserInfo(token);
        } catch (Exception e) {
            log.error("ws 认证失败", e);
            return false;
        }
        if (isInit) {
            this.clientUserInfo = clientUserInfo;
//            this.tokenExpiresAt = JwtUtils.getDecodedJWT(token).getExpiresAt().getTime();
            this.tokenExpiresAt = 10000L;
            this.token = token;
        }
        return true;
    }

    /**
     * 认证失败，断开连接
     *
     * @param session session
     */
    protected void sendAuthFailed(Session session) {
        try {
            session.getBasicRemote().sendText("认证失败");
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
