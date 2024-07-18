package com.demo.websocket.springboot.common;

import com.demo.websocket.springboot.way2.ClientUserInfo;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

/**
 * @author jiangyw
 * @date 2024/7/14 17:06
 * @description
 */
public class JwtUtils {

    public static void parseToken(String token){
    }

    public static ClientUserInfo getClientUserInfo(String token){
        return ClientUserInfo.builder().id(1).account("NO.1").build();
    }

}
