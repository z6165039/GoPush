package com.gopush.protocol.node;

import lombok.Builder;

/**
 * go-push
 *
 * @类功能说明：单条消息发送给多个设备
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
public class OneMessageToMultiDeviceResp extends NodeMessageResp<OneMessageToMultiDeviceResp>{
    @Override
    protected Type type() {
        return Type.OTMS;
    }

    @Override
    protected OneMessageToMultiDeviceResp getThis() {
        return this;
    }


}
