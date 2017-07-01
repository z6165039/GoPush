package com.gopush.datacenter.nodes.manager;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.NodeMessage;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
@Builder
public class Node {

    /**
     * 内网IP
     */
    private String intranetIp;

    /**
     * 内网端口
     */
    private int intranetPort;

    /**
     * 对外IP
     */
    private String internetIp;

    /**
     * 对外端口
     */
    private int internetPort;

    /**
     * loopgroup  所有的客户端共用一个
     */
    @Setter
    private transient EventLoopGroup group;

    /**
     * 各自客户端的channel
     */
    private transient Channel channel;

    /**
     * 失败的请求
     */
    private transient Queue<NodeMessage> failMessage;


    /**
     * 处理器设置
     */
    private transient volatile Set<INodeMessageHandler> nodeMessageHandlers;

    /**
     * 连接是否运行
     */
    private volatile boolean runing = Boolean.FALSE;




    public void  init(){
        failMessage = new ConcurrentLinkedQueue<>();

        runing = Boolean.TRUE;

    }





}
