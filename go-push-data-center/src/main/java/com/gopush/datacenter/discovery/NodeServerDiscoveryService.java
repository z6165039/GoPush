package com.gopush.datacenter.discovery;

import com.gopush.common.utils.zk.ZkUtils;
import com.gopush.common.utils.zk.listener.ZkStateListener;
import com.gopush.datacenter.config.GoPushDataCenterConfig;
import com.gopush.datacenter.config.ZookeeperConfig;
import com.gopush.infos.nodeserver.bo.NodeServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author chenxiangqi
 * @date 2017/9/12 上午3:13
 */

@Slf4j
@Component
public class NodeServerDiscoveryService {

    private Map<String,NodeServerInfo> nodeServers = new ConcurrentHashMap<>();

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    @Autowired
    private GoPushDataCenterConfig goPushDataCenterConfig;

    private ZkUtils zkUtils;

    @PostConstruct
    public void init() {
        zkUtils = new ZkUtils();
        zkUtils.init(
                zookeeperConfig.getServers(),
                zookeeperConfig.getConnectionTimeout(),
                zookeeperConfig.getSessionTimeout(),
                zookeeperConfig.getMaxRetries(),
                zookeeperConfig.getRetriesSleepTime(),
                zookeeperConfig.getListenNamespace(),
                new ZkStateListener() {
                    @Override
                    public void connectedEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("链接zk成功");
                    }

                    @Override
                    public void ReconnectedEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("重新链接zk成功");
                    }

                    @Override
                    public void lostEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("链接zk丢失");
                    }
                });

    }

    @PreDestroy
    public void destory() {
        zkUtils.destory();
    }





}
