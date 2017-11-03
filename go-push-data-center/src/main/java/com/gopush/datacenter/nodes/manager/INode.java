package com.gopush.datacenter.nodes.manager;

import com.gopush.protocol.node.BaseNodeMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/7/1 上午8:35
 * @VERSION：
 */
public interface INode {

    void init();

    void destroy();


    void active();

    void inactive();


    void send(BaseNodeMessage message);

    void send(BaseNodeMessage message, boolean retry);


    void retrySendFail();

    void reconnect(ChannelHandlerContext ctx);

    void handle(ChannelHandlerContext ctx, BaseNodeMessage message);


    int receiveCounter();

    int sendCounter();
}
