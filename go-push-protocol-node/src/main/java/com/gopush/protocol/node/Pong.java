package com.gopush.protocol.node;

import lombok.Builder;
/**
 * go-push
 *
 * @类功能说明：Ping-Pong
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Builder
public class Pong extends NodeMessageResp<Pong> {


    @Override
    protected Type type() {
        return Type.PO;
    }

    @Override
    protected Pong getThis() {
        return null;
    }


}
