package com.gopush.datacenter.nodes.inbound;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/24 上午12:23
 * @VERSION：
 */

@Slf4j
@ChannelHandler.Sharable
public class NodeChannelInBoundHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel active, channel:{}",ctx.channel());

        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("channel inactive, channel:{}",ctx.channel());

        super.channelInactive(ctx);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception error:{}, channel:{}",cause,ctx.channel());
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }



}
