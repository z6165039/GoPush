package com.gopush.protocol.device;

import lombok.*;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong 心跳
 * @author：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
@Data
@AllArgsConstructor
public class Ping extends DeviceMessageReq<Ping> {

    @Override
    protected Type type() {
        return Type.PI;
    }

    @Override
    protected Ping getThis() throws Exception {
        return this;
    }


}
