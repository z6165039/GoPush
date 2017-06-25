package com.gopush.nodeserver.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.nodeserver.devices.stores.IDeviceChannelStore;
import com.gopush.protocol.device.PushReq;
import com.gopush.protocol.node.MessageToMultiDeviceReq;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 下午11:00
 * @VERSION：
 */
@Slf4j
public class MessageToMultiDeviceHandler implements INodeMessageHandler<MessageToMultiDeviceReq> {

    @Autowired
    private IDeviceChannelStore deviceChannelStore;

    @Override
    public boolean support(MessageToMultiDeviceReq message) {
        return message instanceof  MessageToMultiDeviceReq;
    }

    @Override
    public void call(ChannelHandlerContext ctx, MessageToMultiDeviceReq message) {

        //找寻到对应设备的channel 将消息全部推送给这个设备
        if (message != null){
            if(CollectionUtils.isNotEmpty(message.getDevices())){
                List<String> devcies = message.getDevices();
                devcies.stream().forEach((e) -> {
                    Channel channel = deviceChannelStore.getChannel(e);
                    if (channel != null) {
                        List<String> msgList = new ArrayList<>();
                        msgList.add(message.getMessage());
                        //构造发送请求
                        PushReq pushReq = PushReq.builder().msgs(msgList)
                                .build();
                        channel.writeAndFlush(pushReq.encode());
                    }else {
                        //将要发给这个设备的消息存一份到redis
                        //没有找到,将该信息存储在redis中,添加超时机制


                    }
                });
            }
        }



    }
}
