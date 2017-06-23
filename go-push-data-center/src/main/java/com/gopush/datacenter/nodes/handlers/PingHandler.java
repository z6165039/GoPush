package com.gopush.datacenter.nodes.handlers;

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
 * @创建时间：2017/6/21
 * @VERSION：
 */
@Slf4j
public class PingHandler implements INodeMessageHandler<Ping> {

    private static final String PONG = Pong.builder().build().encode();
    @Override
    public boolean support(Ping message) {
        return message instanceof Ping;
    }

    @Override
    public void call(ChannelHandlerContext ctx, Ping message) {
        ctx.channel().writeAndFlush(PONG);
        log.debug("receive ping,channel:{}",ctx.channel());
    }
}
