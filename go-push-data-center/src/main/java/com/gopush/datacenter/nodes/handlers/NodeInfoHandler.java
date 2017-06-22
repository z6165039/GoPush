package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.NodeInfoReq;
import io.netty.channel.ChannelHandlerContext;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:09
 * @VERSION：
 */
public class NodeInfoHandler implements INodeMessageHandler<NodeInfoReq> {
    @Override
    public boolean support(NodeInfoReq message) {

        return message instanceof  NodeInfoReq;
    }

    @Override
    public void call(ChannelHandlerContext ctx, NodeInfoReq message) {

    }
}
