package com.gopush.protocol.node;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：多条消息发送给单个设备
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class MultiMessageToOneDeviceReq extends NodeMessageReq<MultiMessageToOneDeviceReq> {
    @Override
    protected Type type() {
        return Type.MTO;
    }

    @Override
    protected MultiMessageToOneDeviceReq getThis() {
        return this;
    }


}
