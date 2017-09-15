package com.gopush.monitor.dymic.discovery;

import com.alibaba.fastjson.JSON;
import com.gopush.common.constants.ZkGroupEnum;
import com.gopush.common.utils.zk.ZkUtils;
import com.gopush.common.utils.zk.listener.ZkStateListener;
import com.gopush.infos.datacenter.bo.DataCenterInfo;
import com.gopush.infos.nodeserver.bo.NodeServerInfo;
import com.gopush.monitor.config.ZookeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/15 下午3:49
 */

@Slf4j
@Component
public class MonitorNodeServerService {

    //缓存的本地服务列表
    private Map<String, NodeServerInfo> monitorNodeServerPool = new ConcurrentHashMap<>();


    @Autowired
    private ZookeeperConfig zookeeperConfig;

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
                zookeeperConfig.getListenNamespaceNodeServer(),
                new ZkStateListener() {
                    @Override
                    public void connectedEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("MonitorNodeServer 链接zk成功");
                    }

                    @Override
                    public void ReconnectedEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("MonitorNodeServer 重新链接zk成功");
                    }

                    @Override
                    public void lostEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("MonitorNodeServer 链接zk丢失");
                    }
                });

        listenNodeServer();
    }

    @PreDestroy
    public void destory() {
        monitorNodeServerPool.clear();
        zkUtils.destory();
    }


    public List<NodeServerInfo> nodeServerLoader() {
        return monitorNodeServerPool.values().stream().sorted().collect(Collectors.toList());
    }

    /**
     * 设置监听发生更新，更新缓存数据，发生新增，删除，更新
     */
    private void listenNodeServer() {
        zkUtils.listenerPathChildrenCache(ZkGroupEnum.NODE_SERVER.getValue(), ((zkclient, event) -> {
            switch (event.getType()) {
                case CHILD_ADDED:
                    addNodeEvent(event);
                    break;
                case CHILD_REMOVED:
                    removeNodeEvent(event);
                    break;
                case CHILD_UPDATED:
                    updateNodeEvent(event);
                    break;
            }
        }));
    }

    private void updateNodeEvent(PathChildrenCacheEvent event) {
        String key = toKey(event);
        NodeServerInfo data = toNodeServerInfo(event);
        log.info(" Monitor node event update! key:{}, data:{}", key, data);
        if (monitorNodeServerPool.containsKey(key)) {
            monitorNodeServerPool.put(key, data);
        }
    }

    private void removeNodeEvent(PathChildrenCacheEvent event) {
        String key = toKey(event);
        NodeServerInfo data = toNodeServerInfo(event);
        log.info(" Monitor node event remove! key:{}, data:{}", key, data);
        if (monitorNodeServerPool.containsKey(key)) {
            monitorNodeServerPool.remove(key);
        }

    }

    private void addNodeEvent(PathChildrenCacheEvent event) {
        String key = toKey(event);
        NodeServerInfo data = toNodeServerInfo(event);
        log.info(" Monitor node event add! key:{}, data:{}", key, data);
        if (!monitorNodeServerPool.containsKey(key)) {
            //开启node,加入到管理器
            monitorNodeServerPool.put(key, data);
        } else {
            log.error(" Monitor node already! {},{}", key, data);
        }
    }


    private String toKey(PathChildrenCacheEvent event) {
        String path = event.getData().getPath();
        return path.substring(path.lastIndexOf("/")).replaceAll("/", "");
    }

    private NodeServerInfo toNodeServerInfo(PathChildrenCacheEvent event) {
        return JSON.parseObject(event.getData().getData(), NodeServerInfo.class);
    }
}
