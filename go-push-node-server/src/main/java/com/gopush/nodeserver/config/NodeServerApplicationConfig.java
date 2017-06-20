package com.gopush.nodeserver.config;

import com.gopush.nodeserver.devices.DeviceServerBootstrap;
import com.gopush.nodeserver.devices.handlers.*;
import com.gopush.nodeserver.devices.inbound.DeviceChannelInboundHandler;
import com.gopush.nodeserver.devices.senders.PushSender;
import com.gopush.nodeserver.devices.stores.DeviceChannelStore;
import com.gopush.nodeserver.nodes.senders.NodeSender;
import com.gopush.nodeserver.nodes.stores.DataCenterChannelStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by x on 2017/6/20.
 */
@Configuration
public class NodeServerApplicationConfig {


    @Bean
    public DeviceDisconnectHandler deviceDisconnectHandler(){
        return  new DeviceDisconnectHandler();
    }

    @Bean
    public DeviceDockedHandler deviceDockedHandler(){
        return  new DeviceDockedHandler();
    }

    @Bean
    public HandShakeHandler handShakeHandler(){
        return  new HandShakeHandler();
    }


    @Bean
    public PingHandler pingHandler(){
        return  new PingHandler();
    }

    @Bean
    public PongHandler pongHandler(){
        return  new PongHandler();
    }

    @Bean
    public PushRespHandler pushRespHandler(){
        return  new PushRespHandler();
    }


    @Bean
    public DeviceChannelStore deviceChannelStore(){

        return  new DeviceChannelStore();
    }


    @Bean
    public PushSender pushSender(){
        return new PushSender();
    }


    @Bean
    public NodeSender nodeSender(){
        return new  NodeSender();
    }


    @Bean
    public DataCenterChannelStore dataCenterChannelStore(){
        return new DataCenterChannelStore();
    }

    @Bean
    public DeviceChannelInboundHandler deviceChannelInboundHandler(){
        return new DeviceChannelInboundHandler();
    }


    @Bean
    public DeviceServerBootstrap deviceServerBootstrap(){
        return new DeviceServerBootstrap();
    }
}
