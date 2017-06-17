package com.gopush.protocol.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：握手响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
@Builder
public class HandShakeResp extends DeviceMessageResp {

    @JSONField(name = "R")
    private int result;

    @Override
    protected Type type() {
        return Type.HSR;
    }

    @Override
    protected String toEncode() throws Exception {
       return JSON.toJSONString(this);
    }


}
