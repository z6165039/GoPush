package com.gopush.protocol.node;

import lombok.*;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong
 * @author：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
@Data
@AllArgsConstructor
public class Ping extends NodeMessageReq<Ping> {


    @Override
    protected Type type() {
        return Type.PI;
    }

    @Override
    protected Ping getThis() {
        return this;
    }


}
