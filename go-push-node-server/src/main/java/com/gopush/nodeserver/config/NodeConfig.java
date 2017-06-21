package com.gopush.nodeserver.config;

import com.gopush.nodeserver.nodes.NodeServerBootstrap;
import com.gopush.nodeserver.nodes.handlers.*;
import com.gopush.nodeserver.nodes.inbound.NodeChannelInBoundHandler;
import com.gopush.nodeserver.nodes.senders.NodeSender;
import com.gopush.nodeserver.nodes.stores.DataCenterChannelStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午1:28
 * @VERSION：
 */
@Configuration
public class NodeConfig {

    @Bean
    public NodeServerBootstrap nodeServerBootstrap(){
        return new NodeServerBootstrap();
    }

    @Bean
    public DataCenterChannelStore dataCenterChannelStore(){
        return new DataCenterChannelStore();
    }


    @Bean
    public NodeSender nodeSender(){
        return new  NodeSender();
    }

    @Bean
    public NodeChannelInBoundHandler nodeChannelInBoundHandler(){
        return new NodeChannelInBoundHandler();
    }


    @Bean
    public MessageToMultiDeviceHandler messageToMultiDeviceHandler(){


        return  new MessageToMultiDeviceHandler();
    }

    @Bean
    public MultiMessageToDeviceHandler multiMessageToDeviceHandler(){

        return new MultiMessageToDeviceHandler();
    }

    @Bean
    public NodeDeviceDockedHandler nodeDeviceDockedHandler(){

        return  new  NodeDeviceDockedHandler();
    }

    @Bean
    public NodeDeviceDisconnectHandler nodeDeviceDisconnectHandler(){

        return new NodeDeviceDisconnectHandler();
    }

    @Bean
    public NodeInfoHandler nodeInfoHandler(){

        return new NodeInfoHandler();
    }

    @Bean
    public PingHandler pingHandler(){
        return  new PingHandler();
    }


    @Bean
    public PongHandler pongHandler(){
        return new PongHandler();
    }

}
