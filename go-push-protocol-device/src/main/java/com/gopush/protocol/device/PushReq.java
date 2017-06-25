package com.gopush.protocol.device;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：推送消息请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
public class PushReq extends DeviceMessageReq<PushReq> {

    @Getter
    @JSONField(name = "ID")
    private String id;

    @Getter
    @JSONField(name = "MCS")
    private List<String> msgs;

    @Override
    protected Type type() {
        return Type.P;
    }

    @Override
    protected PushReq getThis() throws Exception {
        return this;
    }




}
