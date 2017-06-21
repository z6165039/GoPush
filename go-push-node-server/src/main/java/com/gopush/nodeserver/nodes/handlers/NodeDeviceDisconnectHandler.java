package com.gopush.nodeserver.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.DeviceDisconResp;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明： 处理 data center 返回的 设备断连的响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 下午11:00
 * @VERSION：
 */
@Slf4j
public class NodeDeviceDisconnectHandler implements INodeMessageHandler<DeviceDisconResp> {
    @Override
    public boolean support(DeviceDisconResp message) {
        return message instanceof  DeviceDisconResp;
    }

    @Override
    public void call(ChannelHandlerContext ctx, DeviceDisconResp message) {

    }
}
