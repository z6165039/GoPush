package com.gopush.protocol.node;

import lombok.Builder;

/**
 * go-push
 *
 * @类功能说明：多条消息发送给单个设备(连接在单个node上的)
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
public class MultiMessageToDeviceReq extends NodeMessageReq<MultiMessageToDeviceReq> {
    @Override
    protected Type type() {
        return Type.MTO;
    }

    @Override
    protected MultiMessageToDeviceReq getThis() {
        return this;
    }


}
