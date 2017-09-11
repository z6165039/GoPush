package com.gopush.nodeserver.infos.watchdog;

import com.gopush.common.utils.ip.IpUtils;
import com.gopush.infos.nodeserver.bo.NodeServerInfo;
import com.gopush.nodeserver.config.GoPushConfig;
import com.gopush.nodeserver.devices.BatchProcessor;
import com.gopush.nodeserver.devices.stores.IDeviceChannelStore;
import com.gopush.infos.nodeserver.bo.NodeLoaderInfo;
import com.gopush.nodeserver.infos.watchdog.listener.event.NodeServerInfoEvent;
import com.gopush.nodeserver.nodes.senders.INodeSender;
import com.gopush.nodeserver.nodes.stores.IDataCenterChannelStore;
import com.gopush.protocol.node.NodeInfoReq;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

/**
 * @author chenxiangqi
 * @date 2017/9/10 下午2:16
 */
@Slf4j
@Component
public class NodeServerInfoWatchdog {

    @Autowired
    private GoPushConfig goPushConfig;

    @Autowired
    private INodeSender nodeSender;

    @Autowired
    private IDeviceChannelStore deviceChannelStore;

    @Autowired
    private IDataCenterChannelStore dataCenterChannelStore;

    @Autowired
    private List<BatchProcessor> deviceMessageHandlers;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Setter
    private int delay = 2000;

    private Timer timer;

    @PostConstruct
    public void init() {
        timer = new Timer("SendNodeServerInfo-Timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //将NodeServer全部信息加载到 zk 中
                //将负载加载到ZK中
                applicationEventPublisher.publishEvent(
                        NodeServerInfoEvent.builder()
                                .name(goPushConfig.getName())
                                .nodeServerInfo(watch())
                                .build());
//                写入zk 其实不需要发送 NodeInfoReq
                nodeSender.send(NodeInfoReq.builder().build());
            }
        }, delay, delay);
    }


    @PreDestroy
    public void destory() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    /**
     * 获取系统负载信息
     *
     * @return
     */
    public NodeServerInfo watch() {
        return NodeServerInfo.builder()
                .name(goPushConfig.getName())
                .internetIp(IpUtils.internetIp())
                .intranetIp(IpUtils.intranetIp())
                .devicePort(goPushConfig.getDevicePort())
                .nodePort(goPushConfig.getNodePort())
                .nodeLoaderInfo(NodeLoaderInfo.builder()
                        .onlineDcCounter(dataCenterChannelStore.count())
                        .onlineDeviceCounter(deviceChannelStore.count())
                        .handlerInfos(deviceMessageHandlers.stream().map(BatchProcessor::getHandlerInfo).collect(Collectors.toList()))
                        .build()
                )
                .build();
    }
}
