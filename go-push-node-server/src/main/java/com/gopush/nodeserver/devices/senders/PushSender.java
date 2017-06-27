package com.gopush.nodeserver.devices.senders;

import com.gopush.nodeserver.devices.stores.IDeviceChannelStore;
import com.gopush.protocol.device.PushReq;
import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/18 下午11:32
 * @VERSION：
 */
@Slf4j
public class PushSender implements IPushSender<PushReq> {


    @Autowired
    IDeviceChannelStore deviceChannelStore;

    @Override
    public void send(String device, PushReq message) {
        Channel channel = deviceChannelStore.getChannel(device);
        if (channel == null){
            log.warn("can not find channel, device :{}",device);
            return;
        }

        channel.writeAndFlush(message.encode()).addListener((channelFuture) -> {
            if (!channelFuture.isSuccess()){
                log.error("send message error, device:{}, msg_id:{}, msg:{} ",device,message.getId(),message.getMsgs());
                // TODO: 2017/6/19  这边可以做重试操作
                //并且记录错误次数

            }else {
                log.debug("send message successful, device:{}, msg_id:{}, msg:{} ",device,message.getId(),message.getMsgs());
            }
        });
    }
}
