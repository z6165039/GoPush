package com.gopush.nodeserver.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.Pong;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:00
 * @VERSION：
 */

@Slf4j
public class NodePongHandler implements INodeMessageHandler<Pong>{
    @Override
    public boolean support(Pong message) {
        return message instanceof Pong;
    }

    @Override
    public void call(Pong message) {

    }
}
