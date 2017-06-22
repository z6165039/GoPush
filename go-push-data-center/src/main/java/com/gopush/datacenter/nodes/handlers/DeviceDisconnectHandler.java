package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.DeviceDisconReq;
import io.netty.channel.ChannelHandlerContext;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:03
 * @VERSION：
 */
public class DeviceDisconnectHandler implements INodeMessageHandler<DeviceDisconReq> {

    @Override
    public boolean support(DeviceDisconReq message) {
        return message instanceof  DeviceDisconReq;
    }

    @Override
    public void call(ChannelHandlerContext ctx, DeviceDisconReq message) {

    }
}
