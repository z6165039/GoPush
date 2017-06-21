package com.gopush.nodeserver.nodes.handlers;


import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.NodeInfoResp;
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
public class NodeInfoHandler implements INodeMessageHandler<NodeInfoResp> {
    @Override
    public boolean support(NodeInfoResp message) {
        return message instanceof NodeInfoResp;
    }

    @Override
    public void call(ChannelHandlerContext ctx, NodeInfoResp message) {

    }
}
