package com.gopush.protocol.node;

import lombok.Builder;

/**
 * go-push
 *
 * @类功能说明：多条消息发送给单个设备
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
public class MultiMessageToOneDeviceResp extends NodeMessageResp<MultiMessageToOneDeviceResp>{
    @Override
    protected Type type() {
        return Type.MTOS;
    }

    @Override
    protected MultiMessageToOneDeviceResp getThis() {
        return this;
    }


}
