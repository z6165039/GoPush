package com.gopush.nodeserver.devices;

import com.gopush.devices.handlers.IDeviceMessageHandler;
import com.gopush.nodeserver.devices.handlers.*;
import com.gopush.nodeserver.devices.inbound.DeviceChannelInboundHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;

/**
 * go-push
 *
 * @类功能说明：设备服务启动器
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/18 下午10:59
 * @VERSION：
 */

@Slf4j
@Builder
public class DeviceServerBootstrap {

    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workGroup = new NioEventLoopGroup();


    private int port;


    @Autowired
    private DeviceDisconnectHandler deviceDisconnectHandler;

    @Autowired
    private HandShakeHandler handShakeHandler;

    @Autowired
    private PingHandler pingHandler;

    @Autowired
    private PongHandler pongHandler;

    @Autowired
    private PushRespHandler pushRespHandler;

    @PostConstruct
    public void start() throws Exception {

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workGroup)
                .channelFactory(NioServerSocketChannel::new)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {

                        List<IDeviceMessageHandler> deviceHandlers = new ArrayList<>();
                        deviceHandlers.add(handShakeHandler);
                        deviceHandlers.add(pingHandler);
                        deviceHandlers.add(pongHandler);
                        deviceHandlers.add(pushRespHandler);
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast("logHandler",new LoggingHandler());
                        pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                        pipeline.addLast("stringDecoder",new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast("frameEncoder",new LengthFieldPrepender(4));
                        pipeline.addLast("stringEncoder",new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast("idleStateHandler", new IdleStateHandler(300,0,0));

                        pipeline.addLast("handler",
                                DeviceChannelInboundHandler
                                        .builder()
                                        .deviceDisconnectHandler(deviceDisconnectHandler)
                                        .deviceMessageHandlers(deviceHandlers)
                                        .build());
                    }
                })

                .option(ChannelOption.SO_BACKLOG,1000000)  //连接队列深度
                .option(ChannelOption.TCP_NODELAY, true)   //设置 no_delay
                .childOption(ChannelOption.TCP_NODELAY,true)  //设置 channel no_delay
                .childOption(ChannelOption.SO_REUSEADDR,true) //设置可重用 time wait的 socket
                .option(ChannelOption.SO_SNDBUF,2048).option(ChannelOption.SO_RCVBUF,1024)
                .childOption(ChannelOption.SO_SNDBUF,2048).childOption(ChannelOption.SO_RCVBUF,1024)

                .childOption(ChannelOption.SO_LINGER,0);

                bootstrap.bind(port).sync();
                log.info("Device server start successful!  listening port: {}",port);
    }



    @PreDestroy
    public void destory(){
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
