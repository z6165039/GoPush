package com.gopush.datacenter.nodes.manager;

import com.gopush.protocol.node.NodeMessage;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/7/1 上午8:35
 * @VERSION：
 */
public interface INode {
    void init();

    boolean isAlive();

    void destroy();

    void send(NodeMessage message);

    void send(NodeMessage message, boolean retry);
}
