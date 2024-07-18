package com.demo.websocket.springboot.way3;

import com.demo.websocket.springboot.common.JwtUtils;
import com.demo.websocket.springboot.way2.ClientUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * @author jiangyw
 * @date 2024/7/15 20:02
 * @description 创建拦截器，在做请求参数，或者权限认证的时候，不用在建立链接的函数afterConnectionEstablished里面去处理
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    /**
     * 建立请求之前，可以用来做权限认证
     * @param request the current request
     * @param response the current response
     * @param wsHandler the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket session; the provided
     *                  attributes are copied, the original map is not used.
     * @return 通过/不通过
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler
            , Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            //这里是从请求参数中取token，形如?token=XXX,
            // 也可用 servletServerHttpRequest.getServletRequest().getHeader(HttpHeaders.AUTHORIZATION)从headers中取
            String token = servletServerHttpRequest.getServletRequest().getParameter("token");

            try {
                //从token中解析出用户信息
                ClientUserInfo userInfo = JwtUtils.getClientUserInfo(token);
                //设置当前这个session的属性，后续我们在发送消息时，可以通过这个session.getAttributes().get("clientUserInfo")取出clientUserInfo信息
                attributes.put("clientUserInfo", userInfo);
            }catch (Exception e){
                log.error("websocket认证失败", e);
                return false;
            }
            //todo 可以添加一些其他的校验，成功之后放回true，代表认证通过
            return true;
        }
        return false;
    }

    /**
     * 建立请求之后
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                               Exception exception) {
        //什么都不做
    }
}
