package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.Pong;


/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 
 * @VERSION：
 */
public class PongHandler implements INodeMessageHandler<Pong> {
    @Override
    public boolean support(Pong message) {
        return message instanceof Pong;
    }

    @Override
    public void call(Pong message) {

    }
}
