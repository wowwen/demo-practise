package com.demo.gateway.gray;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @date 2025/4/27 11:17
 * @description
 */
@RefreshScope // 支持配置热更新
@ConfigurationProperties(prefix = "gray-release")
@Data
public class GrayReleaseProperties {
    /**
     * 是否启用灰度
     */
    private boolean enabled;
    /**
     *  Header匹配规则
     */
    private Map<String, String> headerRules = new HashMap<>();
    /**
     * 灰度用户白名单
     */
    private List<String> grayUsers = new ArrayList<>();
    /**
     * 默认灰度流量比例
     */
    private double defaultGrayRatio = 0.1;
}
