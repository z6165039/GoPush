package com.gopush.nodeserver.nodes.inbound;

import com.gopush.nodeserver.nodes.handlers.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 下午9:33
 * @VERSION：
 */

@Slf4j
@ChannelHandler.Sharable
public class NodeChannelInBoundHandler extends SimpleChannelInboundHandler<String>{

    @Autowired
    private MessageToMultiDeviceHandler messageToMultiDeviceHandler;

    @Autowired
    private MultiMessageToDeviceHandler multiMessageToDeviceHandler;

    @Autowired
    private NodeDeviceDisconnectHandler deviceDisconnectHandler;

    @Autowired
    private NodeDeviceDockedHandler deviceDockedHandler;

    @Autowired
    private PingHandler pingHandler;

    @Autowired
    private PongHandler pongHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

    }
}
