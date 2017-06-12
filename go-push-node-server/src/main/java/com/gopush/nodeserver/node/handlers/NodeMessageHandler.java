package com.gopush.nodeserver.node.handlers;

import com.gopush.protocol.node.NodeMessage;

/**
 * go-push
 *
 * @类功能说明：node 节点业务抽象接口
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 上午12:28
 * @VERSION：
 */
public interface NodeMessageHandler {

    /**
     * 根据各个handler 判断是不是各个handler对应处理的消息
     * @param message
     * @return
     */
    boolean is(NodeMessage message);



    void invoke(NodeMessage nodeMessage)


}
