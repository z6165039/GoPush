package com.gopush.nodeserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenxiangqi
 * @date 2017/9/11 下午11:02
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "go-push.node-server")
public class GoPushConfig {
    private String name;
    private int nodePort;
    private int devicePort;
}
