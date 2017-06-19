package com.gopush.nodeserver.config;

import com.gopush.nodeserver.devices.handlers.*;
import com.gopush.nodeserver.devices.senders.PushSender;
import com.gopush.nodeserver.devices.stores.DeviceChannelStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/19 下午11:50
 * @VERSION：
 */

@Configuration
public class DeviceConfig {


    //**************** 业务 Handler ****************/
    @Bean
    public DeviceDisconnectHandler deviceDisconnectHandler(){

        DeviceDisconnectHandler deviceDisconnectHandler = DeviceDisconnectHandler.builder().build();
        return deviceDisconnectHandler;
    }

    @Bean
    public DeviceDockedHandler deviceDockedHandler(){
        DeviceDockedHandler deviceDockedHandler = DeviceDockedHandler.builder().build();


        return deviceDockedHandler;
    }

    @Bean
    public HandShakeHandler handShakeHandler(){
        HandShakeHandler handShakeHandler = HandShakeHandler.builder().build();


        return  handShakeHandler;
    }

    @Bean
    public PingHandler pingHandler(){
        PingHandler pingHandler = PingHandler.builder().build();


        return pingHandler;
    }

    @Bean
    public PongHandler pongHandler(){
        PongHandler pongHandler = PongHandler.builder().build();


        return pongHandler;
    }


    //**************** 存储器 ****************/

    @Bean
    public PushSender pushSender(){
        PushSender pushSender = PushSender.builder().build();
        return pushSender;
    }


    //设备-channel 存储器
    @Bean
    public DeviceChannelStore deviceChannelStore(){
        DeviceChannelStore deviceChannelStore = DeviceChannelStore.builder().build();
        return deviceChannelStore;
    }

}
