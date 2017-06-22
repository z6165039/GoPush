package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.MultiMessageToDeviceReq;
import io.netty.channel.ChannelHandlerContext;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:06
 * @VERSION：
 */
public class MultiMessageToDeviceHandler implements INodeMessageHandler<MultiMessageToDeviceReq> {
    @Override
    public boolean support(MultiMessageToDeviceReq message) {
        return message instanceof  MultiMessageToDeviceReq;
    }

    @Override
    public void call(ChannelHandlerContext ctx, MultiMessageToDeviceReq message) {

    }
}
