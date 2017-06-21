package com.gopush.nodeserver.config;

import com.gopush.nodeserver.devices.DeviceServerBootstrap;
import com.gopush.nodeserver.devices.handlers.*;
import com.gopush.nodeserver.devices.inbound.DeviceChannelInboundHandler;
import com.gopush.nodeserver.devices.senders.PushSender;
import com.gopush.nodeserver.devices.stores.DeviceChannelStore;
import org.springframework.context.annotation.Bean;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午1:28
 * @VERSION：
 */
public class DeviceConfig {

    /*    */
    @Bean
    public DeviceServerBootstrap deviceServerBootstrap(){
        return new DeviceServerBootstrap();
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
    public DeviceChannelInboundHandler deviceChannelInboundHandler(){
        return new DeviceChannelInboundHandler();
    }



    @Bean
    public DeviceDeviceDisconnectHandler deviceDisconnectHandler(){
        return  new DeviceDeviceDisconnectHandler();
    }

    @Bean
    public DeviceDeviceDockedHandler deviceDockedHandler(){
        return  new DeviceDeviceDockedHandler();
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


}
