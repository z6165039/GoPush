package com.gopush.protocol.device;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;

/**
 * go-push
 *
 * @类功能说明：握手请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Builder
public class HandShakeReq extends DeviceMessageReq<HandShakeReq> {

    @Getter
    @JSONField(name = "D")
    private String device;

    @Getter
    @JSONField(name = "TK")
    private String token;

    @Getter
    @JSONField(name = "R_IDLE")
    private int readInterval;
    @Getter
    @JSONField(name = "W_IDLE")
    private int writeInterval;
    @Getter
    @JSONField(name = "A_IDLE")
    private int allInterval;


    @Override
    protected Type type() {
        return Type.HS;
    }

    @Override
    protected HandShakeReq getThis() throws Exception {
        return this;
    }


}
