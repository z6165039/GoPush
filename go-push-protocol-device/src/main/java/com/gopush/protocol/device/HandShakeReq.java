package com.gopush.protocol.device;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：握手请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
@Builder
public class HandShakeReq extends DeviceMessageReq<HandShakeReq> {

    @JSONField(name = "D")
    private String device;

    @JSONField(name = "TK")
    private String token;


    @Override
    protected Type type() {
        return Type.HS;
    }

    @Override
    protected HandShakeReq getThis() throws Exception {
        return this;
    }


}
