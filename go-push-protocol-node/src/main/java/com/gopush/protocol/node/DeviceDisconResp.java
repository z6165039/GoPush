package com.gopush.protocol.node;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：设备下线上报响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class DeviceDisconResp extends NodeMessageResp<DeviceDisconResp>{

    @JSONField(name = "R")
    private int result;

    @Override
    protected Type type() {
        return Type.DIS;
    }

    @Override
    protected DeviceDisconResp getThis() {
        return this;
    }


}
