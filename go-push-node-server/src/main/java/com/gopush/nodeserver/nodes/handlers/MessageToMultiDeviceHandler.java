package com.gopush.nodeserver.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.MessageToMultiDeviceReq;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;


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
    @Override
    public boolean support(MessageToMultiDeviceReq message) {
        return message instanceof  MessageToMultiDeviceReq;
    }

    @Override
    public void call(ChannelHandlerContext ctx, MessageToMultiDeviceReq message) {

    }
}
