package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.DeviceDockedReq;
import io.netty.channel.ChannelHandlerContext;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:05
 * @VERSION：
 */
public class DeviceDockedHandler implements INodeMessageHandler<DeviceDockedReq> {
    @Override
    public boolean support(DeviceDockedReq message) {
        return message instanceof  DeviceDockedReq;
    }

    @Override
    public void call(ChannelHandlerContext ctx, DeviceDockedReq message) {

    }
}
