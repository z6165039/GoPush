package com.gopush.datacenter.nodes.manager.listener;

import com.gopush.datacenter.dymic.discovery.NodeServerDiscoveryService;
import com.gopush.datacenter.nodes.manager.NodeManager;
import com.gopush.datacenter.nodes.manager.listener.event.ReloadNodesEvent;
import com.gopush.infos.nodeserver.bo.NodeServerInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/12 下午11:09
 */
@Slf4j
@Component
public class ReloadNodesListener {

    @Autowired
    private NodeServerDiscoveryService nodeServerDiscoveryService;

    @Autowired
    private NodeManager nodeManager;

    @EventListener
    public void reloadNodeManager(ReloadNodesEvent event) {
        log.info(".................................1");
        log.info(".................................2");
        log.info(".................................3");
        log.info(".................................4 {}",event.getEventName());

        Map<String, NodeServerInfo> maps = nodeServerDiscoveryService.nodeServerPool();
        maps.forEach((k, v) -> nodeManager.put(k, v.getIntranetIp(), v.getNodePort(), v.getInternetIp(), v.getDevicePort()));
    }
}
