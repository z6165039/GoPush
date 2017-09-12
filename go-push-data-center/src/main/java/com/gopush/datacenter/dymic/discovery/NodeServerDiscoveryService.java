package com.gopush.datacenter.dymic.discovery;

import com.alibaba.fastjson.JSON;
import com.gopush.common.constants.ZkGroupEnum;
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

    private Map<String, NodeServerInfo> nodeServers = new ConcurrentHashMap<>();

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
        initNodeServerDiscovery();
        listenNodeServerDiscovery();
    }

    @PreDestroy
    public void destory() {
        zkUtils.destory();
    }


    /**
     * 初始化node-server列表
     */
    private void initNodeServerDiscovery() {
        nodeServers.clear();
        Map<String, String> datas = zkUtils.readTargetChildsData(ZkGroupEnum.NODE_SERVER.getValue());
        if (datas != null) {
            datas.forEach((k, v) -> nodeServers.put(k, JSON.parseObject(v, NodeServerInfo.class)));
        }
    }

    /**
     * 设置监听发生更新，更新缓存数据，发生新增，删除，更新
     */
    private void listenNodeServerDiscovery() {
        zkUtils.listenerPathChildrenCache(ZkGroupEnum.NODE_SERVER.getValue(), ((client, event) -> {
            String path = event.getData().getPath();
            NodeServerInfo data = JSON.parseObject(event.getData().getData(), NodeServerInfo.class);
            switch (event.getType()) {
                case CHILD_ADDED:
                    addNodeEvent(path, data);
                    break;
                case CHILD_REMOVED:
                    removeNodeEvent(path, data);
                    break;
                case CHILD_UPDATED:
                    updateNodeEvent(path, data);
                    break;
            }
        }));
    }

    private void addNodeEvent(String path, NodeServerInfo data) {

    }

    private void updateNodeEvent(String path, NodeServerInfo data) {

    }

    private void removeNodeEvent(String path, NodeServerInfo data) {
    }


}
