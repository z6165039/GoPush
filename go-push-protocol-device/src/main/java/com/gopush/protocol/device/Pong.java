package com.gopush.protocol.device;

import lombok.Builder;
import lombok.Getter;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong 心跳
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
@Getter
public class Pong extends DeviceMessageResp<Pong> {


    @Override
    protected Type type() {
        return Type.PO;
    }

    @Override
    protected Pong getThis() throws Exception {
        return null;
    }



}
