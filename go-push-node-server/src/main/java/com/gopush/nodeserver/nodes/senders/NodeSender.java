package com.gopush.nodeserver.nodes.senders;

import com.gopush.nodeserver.nodes.stores.IDataCenterChannelStore;
import com.gopush.protocol.node.NodeMessage;
import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/19 上午1:04
 * @VERSION：
 */

@Slf4j
public class NodeSender implements INodeSender<NodeMessage> {


    private Queue<InnerMessageInfo> failMessage = new ConcurrentLinkedQueue<>();

    @Autowired
    private IDataCenterChannelStore dataCenterChannelStore;

    @Setter
    private int delay = 1000;

    private Timer timer;

    @PostConstruct
    public  void init(){
        timer = new Timer("SendNodeMessage-Fail-Retry");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    if(failMessage.isEmpty()){
                        return;
                    }
                    failMessage.stream().forEach((e) -> {
                        switch (e.st){
                            case ALL:
                                send(e.message);
                                break;
                            case ZD:
                                send(e.dcId,e.message);
                                break;
                            case SJO:
                                sendShuffle(e.message);
                                break;
                            case SJM:
                                sendShuffle(e.dcCount,e.message);
                                break;
                        }
                    });
                }catch (Exception e){
                    log.error("Exception error:{}",e);
                }
            }
        }, delay, delay);
    }

    @PreDestroy
    public void destory(){
        failMessage.clear();
    }



    @Override
    public void send(NodeMessage message) {
        if (dataCenterChannelStore.count() > 0){
            List<Channel> list = dataCenterChannelStore.getAllChannels();
            if (list != null){
                list.stream().filter( channel -> channel!=null ).forEach((channel)->{
                    channel.writeAndFlush(message.encode());
                });
            }
        }else{
            log.warn("can not find data center, retry later!");
            addFailMessage(message,SendType.ALL,null,0);
        }
    }

    @Override
    public void send(String dcId, NodeMessage message) {
        if (dataCenterChannelStore.count() > 0){
            if (dataCenterChannelStore.contains(dcId)){
                Channel channel = dataCenterChannelStore.getChannel(dcId);
                channel.writeAndFlush(message.encode());
            }else {
                addFailMessage(message,SendType.ZD,dcId,0);
            }
        }else{
            log.warn("can not find data center, retry later!");
            addFailMessage(message,SendType.SJO,null,0);
        }
    }

    @Override
    public void sendShuffle(int dcConunt, NodeMessage message) {
        if(dcConunt == 1){
            sendShuffle(message);
            return;
        }
        if (dataCenterChannelStore.count() > 0){
            List<Channel> list = new ArrayList<>(dataCenterChannelStore.getAllChannels());
            List<Channel> targets = new ArrayList<>();
            do {
                Collections.shuffle(list);
                targets.add(list.get(0));
                dcConunt--;
            }while (dcConunt >=0 );
            targets.stream().forEach((channel) ->{
                channel.writeAndFlush(message.encode());
            });
        }else{
            log.warn("can not find data center, retry later!");
            addFailMessage(message,SendType.SJM,null,dcConunt);
        }
    }

    @Override
    public void sendShuffle(NodeMessage message) {
        if (dataCenterChannelStore.count() > 0){
            List<Channel> list = new ArrayList<>(dataCenterChannelStore.getAllChannels());
            Collections.shuffle(list);
            Channel channel = list.get(0);
            channel.writeAndFlush(message.encode());
        }else{
            log.warn("can not find data center, retry later!");
            addFailMessage(message,SendType.SJO,null,0);
        }
    }



    private void addFailMessage(NodeMessage message, SendType st,String dcId,int dcCount){

        InnerMessageInfo.InnerMessageInfoBuilder builder =  InnerMessageInfo
                .builder()
                .message(message)
                .st(st);
        switch (st){
            case ZD:
                builder.dcId(dcId);
                break;
            case SJM:
                builder.dcCount(dcCount);
                break;
        }
        InnerMessageInfo info = builder.build();
        if(!failMessage.contains(info)){
            failMessage.add(info);
        }
    }


    private enum SendType{
        ALL,
        ZD,
        SJO,
        SJM
    }

    @Builder
    private class InnerMessageInfo{
        private SendType st;
        private NodeMessage message;
        private String dcId;
        private int dcCount;
    }
}
