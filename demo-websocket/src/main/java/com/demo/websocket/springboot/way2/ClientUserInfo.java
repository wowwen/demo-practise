package com.demo.websocket.springboot.way2;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiangyw
 * @date 2024/7/13 22:49
 * @description
 */
@Data
@Builder
public class ClientUserInfo {
    private Integer id;

    private String account;
}
