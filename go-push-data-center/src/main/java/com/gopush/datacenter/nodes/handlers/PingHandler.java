package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.Ping;


/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21
 * @VERSION：
 */
public class PingHandler implements INodeMessageHandler<Ping> {
    @Override
    public boolean support(Ping message) {
        return message instanceof Ping;
    }

    @Override
    public void call(Ping message) {

    }
}
