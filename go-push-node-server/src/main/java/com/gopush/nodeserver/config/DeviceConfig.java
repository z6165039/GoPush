package com.gopush.nodeserver.config;

import com.gopush.nodeserver.devices.DeviceServerBootstrap;
import com.gopush.nodeserver.devices.handlers.*;
import com.gopush.nodeserver.devices.inbound.DeviceChannelInboundHandler;
import com.gopush.nodeserver.devices.senders.PushSender;
import com.gopush.nodeserver.devices.stores.DeviceChannelStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * go-push
 *
 * @类功能说明：设备处理器配置类
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午1:28
 * @VERSION：
 */

@Configuration
public class DeviceConfig {





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
    public DevicePingHandler devicePingHandler(){
        return  new DevicePingHandler();
    }

    @Bean
    public DevicePongHandler devicePongHandler(){
        return  new DevicePongHandler();
    }

    @Bean
    public PushRespHandler pushRespHandler(){
        return  new PushRespHandler();
    }


}
