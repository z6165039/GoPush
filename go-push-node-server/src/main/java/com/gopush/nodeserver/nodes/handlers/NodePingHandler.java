package com.gopush.nodeserver.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.Ping;
import com.gopush.protocol.node.Pong;
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
public class NodePingHandler implements INodeMessageHandler<Ping> {

    private static final String PONG = Pong.builder().build().encode();
    @Override
    public boolean support(Ping message) {
        return message instanceof Ping;
    }

    @Override
    public void call(ChannelHandlerContext ctx, Ping message) {
        ctx.channel().writeAndFlush(message);
        log.debug("node send ping to data center, channel:{}",ctx.channel());
    }
}
