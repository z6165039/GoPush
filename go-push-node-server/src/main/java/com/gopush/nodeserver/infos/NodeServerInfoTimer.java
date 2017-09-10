package com.gopush.nodeserver.infos;

import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.nodeserver.devices.stores.IDeviceChannelStore;
import com.gopush.nodeserver.infos.bo.NodeServerInfo;
import com.gopush.nodeserver.nodes.senders.INodeSender;
import com.gopush.nodeserver.nodes.stores.IDataCenterChannelStore;
import com.gopush.protocol.node.NodeInfoReq;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NodeServerInfoTimer {

    @Autowired
    private INodeSender nodeSender;

    @Autowired
    private IDeviceChannelStore deviceChannelStore;

    @Autowired
    private IDataCenterChannelStore dataCenterChannelStore;

    @Autowired
    private List<BatchProcesser> deviceMessageHandlers;

    @Setter
    private int delay = 2000;

    private Timer timer;

    @PostConstruct
    public void init(){
        timer = new Timer("SendNodeServerInfo-Timer");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //将NodeServer全部信息加载到 zk 中
                NodeServerInfo serverInfo = NodeServerInfo.builder()
                        .onlineDcCounter(dataCenterChannelStore.count())
                        .onlineDeviceCounter(deviceChannelStore.count())
                        .handlerInfos(deviceMessageHandlers.stream().map(BatchProcesser::getHandlerInfo).collect(Collectors.toList()))
                        .build();

                //将负载加载到ZK中
                //todo

                NodeInfoReq infoReq = NodeInfoReq.builder().build();
                //写入zk 其实不需要发送 NodeInfoReq
                nodeSender.send(infoReq);
            }
        }, delay, delay);
    }


    @PreDestroy
    public void destory(){
        if(timer != null ){
            timer.cancel();
            timer = null;
        }
    }
}
