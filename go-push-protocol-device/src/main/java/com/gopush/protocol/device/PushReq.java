package com.gopush.protocol.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：推送消息请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class PushReq extends DeviceMessageReq<PushReq> {

    @JSONField(name = "ID")
    private String id;

    @JSONField(name = "MC")
    private String msg;

    @Override
    protected Type type() {
        return Type.P;
    }

    @Override
    protected PushReq getThis() throws Exception {
        return this;
    }




}
