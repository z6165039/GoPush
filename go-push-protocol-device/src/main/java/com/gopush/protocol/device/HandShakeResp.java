package com.gopush.protocol.device;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;

/**
 * go-push
 *
 * @类功能说明：握手响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Builder
@Getter
public class HandShakeResp extends DeviceMessageResp<HandShakeResp> {

    @JSONField(name = "R")
    private int result;

    @Override
    protected Type type() {
        return Type.HSR;
    }

    @Override
    protected HandShakeResp getThis() throws Exception {
        return this;
    }


}
