package com.gopush.protocol.node;

import lombok.Builder;

/**
 * go-push
 *
 * @类功能说明：单条消息发送给多个设备(连接在单个node上的)
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
public class MessageToMultiDeviceReq extends NodeMessageReq<MessageToMultiDeviceReq>{
    @Override
    protected Type type() {
        return Type.OTM;
    }

    @Override
    protected MessageToMultiDeviceReq getThis() {
        return this;
    }


}
