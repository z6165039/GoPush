package com.gopush.datacenter.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.MultiMessageToDeviceReq;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/21 下午11:07
 * @VERSION：
 */
public class MessageToMultiDeviceHandler implements INodeMessageHandler<MultiMessageToDeviceReq> {
    @Override
    public boolean support(MultiMessageToDeviceReq message) {
        return message instanceof  MultiMessageToDeviceReq;
    }

    @Override
    public void call(MultiMessageToDeviceReq message) {

    }
}
