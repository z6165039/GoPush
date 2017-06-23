package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.MultiMessageToDeviceResp;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:06
 * @VERSION：
 */

@Slf4j
public class MultiMessageToDeviceHandler implements INodeMessageHandler<MultiMessageToDeviceResp> {
    @Override
    public boolean support(MultiMessageToDeviceResp message) {
        return message instanceof  MultiMessageToDeviceResp;
    }

    @Override
    public void call(ChannelHandlerContext ctx, MultiMessageToDeviceResp message) {

    }
}
