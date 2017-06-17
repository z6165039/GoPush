package com.gopush.protocol.device;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong 心跳
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class Pong extends DeviceMessageResp {


    @Override
    protected Type type() {
        return Type.PO;
    }

    @Override
    protected String toEncode() throws Exception {
        return null;
    }


}
