package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.MessageToMultiDeviceResp;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:07
 * @VERSION：
 */

@Slf4j
public class MessageToMultiDeviceHandler implements INodeMessageHandler<MessageToMultiDeviceResp> {
    @Override
    public boolean support(MessageToMultiDeviceResp message) {
        return message instanceof  MessageToMultiDeviceResp;
    }

    @Override
    public void call(ChannelHandlerContext ctx, MessageToMultiDeviceResp message) {

    }
}
