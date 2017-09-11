package com.gopush.nodeserver.registers;

import com.gopush.common.utils.zk.ZkUtils;
import com.gopush.common.utils.zk.listener.ZkStateListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author chenxiangqi
 * @date 2017/9/10 下午10:42
 */
@Slf4j
@Component
public class NodeServerRegister {

    @Value("${go-push.node-server.zookeeper.server}")
    private String zookeeperServer = "127.0.0.1:2181";

    @Value("${go-push.node-server.zookeeper.max-retries}")
    private int maxRetries= Integer.MAX_VALUE - 1 ;

    @Value("${go-push.node-server.zookeeper.namespace:nodeserver}")
    private String namespace;

    @Value("${go-push.node-server.zookeeper.session-timeout:6000}")
    private int sessionTimeout;

    @Value("${go-push.node-server.zookeeper.connection-timeout:6000}")
    private int connectionTimeout;

    @Value("${go-push.node-server.zookeeper.retries-sleep-time:2000}")
    private int retriesSleepTime;

    @PostConstruct
    public void init(){
        ZkUtils.instance().init(zookeeperServer, connectionTimeout, sessionTimeout, maxRetries, retriesSleepTime, null, new ZkStateListener() {
            @Override
            public void connectedEvent(CuratorFramework curator, ConnectionState state) {
                log.info(".........链接zk成功");
            }

            @Override
            public void ReconnectedEvent(CuratorFramework curator, ConnectionState state) {
                log.info(".........重新链接zk成功");
            }

            @Override
            public void lostEvent(CuratorFramework curator, ConnectionState state) {
                log.info(".........链接zk丢失");
            }
        });

    }

    @PreDestroy
    public void destory(){

    }








}
