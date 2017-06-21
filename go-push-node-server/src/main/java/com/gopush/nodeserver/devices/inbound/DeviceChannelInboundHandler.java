package com.gopush.nodeserver.devices.inbound;

import com.gopush.devices.handlers.IDeviceMessageHandler;
import com.gopush.nodeserver.devices.handlers.*;
import com.gopush.protocol.device.DeviceMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 上午12:46
 * @VERSION：
 */

@Slf4j
@ChannelHandler.Sharable
public class DeviceChannelInboundHandler extends SimpleChannelInboundHandler<String> {

    @Autowired
    private DeviceDeviceDisconnectHandler deviceDisconnectHandler;

    @Autowired
    private HandShakeHandler handShakeHandler;

    @Autowired
    private DevicePingHandler devicePingHandler;

    @Autowired
    private DevicePongHandler devicePongHandler;

    @Autowired
    private PushRespHandler pushRespHandler;

    private List<IDeviceMessageHandler> deviceMessageHandlers = new ArrayList<>();

    @PostConstruct
    public void init(){
        deviceMessageHandlers.add(handShakeHandler);
        deviceMessageHandlers.add(devicePingHandler);
        deviceMessageHandlers.add(devicePongHandler);
        deviceMessageHandlers.add(pushRespHandler);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String message) throws Exception {

        log.debug("Channel {} recevie:{}",ctx.channel(),message);
        DeviceMessage deviceMessage = DeviceMessage.decode(message);

        if(!deviceMessageHandlers.isEmpty()){
            deviceMessageHandlers.stream().forEach((handler) -> {
                try{
                    if(handler.support(deviceMessage)){
                        handler.call(ctx,deviceMessage);
                    }
                }catch (Exception e){
                    log.error("Exception : ",e);
                }
            });
        }

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Connect device ! channel :{}",ctx.channel());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Channel {} inactive!",ctx.channel());
        deviceDisconnectHandler.channelClosed(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exception , channel will be closed ! channel:{}", ctx.channel(),cause.getMessage());
        ctx.close();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())){
            IdleStateEvent event = (IdleStateEvent) evt;


            if (event.state() == IdleState.READER_IDLE){
                //发送心跳
            }
            if (event.state() == IdleState.WRITER_IDLE){
                //发送心跳
            }
            if (event.state() == IdleState.ALL_IDLE){
                //发送心跳
            }
        }
    }


}
