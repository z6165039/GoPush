package com.gopush.springframework.boot.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * go-push
 *
 * @类功能说明：redis-cluster 配置映射POJO
 * @作者：chenxiangqi
 * @创建时间：2017/6/9 下午9:48
 * @VERSION：
 */

@Log4j
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "redis.cluster")
public class RedisClusterProperties {

    private String[] servers;

    private String[] dockedServers;

    private String password;

    private int poolSize;

    private int timeout;

}
