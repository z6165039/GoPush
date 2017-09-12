package com.gopush.datacenter.nodes.manager;

import com.gopush.datacenter.nodes.inbound.NodeChannelInBoundHandler;
import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.NodeMessage;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/7/1 上午8:35
 * @VERSION：
 */

@Slf4j
@EqualsAndHashCode
@Data
public class Node implements INode {

    /**
     * 内网IP
     */
    private String intranetIp;

    /**
     * 内网端口 node-port
     */
    private int nodePort;

    /**
     * 对外IP DEVICE
     */
    private String internetIp;

    /**
     * 对外端口 device-port
     */
    private int devicePort;

    /**
     * loopgroup  所有的客户端共用一个
     */
    private transient EventLoopGroup group;

    /**
     * 处理器设置
     */
    private transient volatile List<INodeMessageHandler> nodeMessageHandlers;

    /**
     * 连接是否运行
     */
    private volatile boolean runing = Boolean.FALSE;

    /**
     * 失败的请求
     */
    private transient Queue<NodeMessage> failMessage = new ConcurrentLinkedQueue<>();


    /**
     * 各自客户端的channel
     */
    private transient Channel channel;

    public Node(String intranetIp, int nodePort,
                String internetIp, int devicePort,
                EventLoopGroup group, List<INodeMessageHandler> nodeMessageHandlers){
        this.intranetIp = intranetIp;
        this.nodePort = nodePort;
        this.internetIp = internetIp;
        this.devicePort = devicePort;
        this.group = group;
        this.nodeMessageHandlers = nodeMessageHandlers;
    }

    @Override
    public void init() {
        if (runing){
            return;
        }
        try{
            connect();
            runing = Boolean.TRUE;
        }catch (Exception e){
            log.error("init data center connect node-server  error:{}",e);
            runing = Boolean.FALSE;
        }

    }

    @Override
    public boolean isAlive() {
        return runing;
    }

    @Override
    public void destroy() {
        if (failMessage != null){
            failMessage.clear();
        }
        failMessage = null;
        nodeMessageHandlers = null;
        group = null;
        if (channel != null){
            if (channel.isActive() || channel.isOpen() || channel.isRegistered()){
                channel.close();
            }
        }
        runing = Boolean.FALSE;
    }

    @Override
    public void send(NodeMessage message) {
        send(message,true);
    }

    @Override
    public void send(NodeMessage message, boolean retry) {
        if(!runing){
            log.warn("node client not init, not running, nodeInfo: {}",toString());
            if (retry){
                failMessage.add(message);
            }
            return;
        }
        if (channel == null){
            log.warn("channel of node is empty! nodeInfo: {}",toString());
            if (retry){
                failMessage.add(message);
            }
            return;
        }
        channel.writeAndFlush(message.encode()).addListener(channelFuture->{
            if (!channelFuture.isSuccess()){
                if (retry){
                    failMessage.add(message);
                }
            }
        });

    }

    private void connect(){
        channel = configBootstrap().connect().channel();
    }

    private Bootstrap configBootstrap(){
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(group)
                .remoteAddress(intranetIp,nodePort)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(channelInitializer());
        return bootstrap;
    }

    private ChannelInitializer channelInitializer(){
        return new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast("stringDecoder", new StringDecoder(CharsetUtil.UTF_8));
                pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                pipeline.addLast("stringEncoder", new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast("idleStateHandler", new IdleStateHandler(300, 0, 0));
                pipeline.addLast("handler",nodeChannelInBoundHandler());
            }
        };
    }

    private NodeChannelInBoundHandler nodeChannelInBoundHandler(){
        return new NodeChannelInBoundHandler(this);
    }

}
