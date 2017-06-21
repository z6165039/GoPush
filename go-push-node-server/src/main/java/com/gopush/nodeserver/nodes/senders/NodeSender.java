package com.gopush.nodeserver.nodes.senders;

import com.gopush.nodeserver.nodes.stores.IDataCenterChannelStore;
import com.gopush.protocol.node.NodeMessage;
import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
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


    private Queue<NodeMessage> failMessage = new ConcurrentLinkedQueue<>();

    @Autowired
    private IDataCenterChannelStore dataCenterChannelStore;




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
            addFailMessage(message);
        }
    }

    @Override
    public void send(String dcId, NodeMessage message) {
        if (dataCenterChannelStore.count() > 0){
            if (dataCenterChannelStore.contains(dcId)){
                Channel channel = dataCenterChannelStore.getChannel(dcId);
                channel.writeAndFlush(message.encode());
            }else {
                addFailMessage(message);
            }
        }else{
            log.warn("can not find data center, retry later!");
            addFailMessage(message);
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
            addFailMessage(message);
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
            addFailMessage(message);
        }
    }

    private void addFailMessage(NodeMessage message){
        if(!failMessage.contains(message)){
            failMessage.add(message);
        }
    }
}
