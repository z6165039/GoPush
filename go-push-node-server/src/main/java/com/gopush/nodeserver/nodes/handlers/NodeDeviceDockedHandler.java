package com.gopush.nodeserver.nodes.handlers;

import com.gopush.nodes.handlers.INodeMessageHandler;
import com.gopush.protocol.node.DeviceDockedResp;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：处理 dataCenter 返回 的数据上报响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/20 下午11:00
 * @VERSION：
 */

@Slf4j
public class NodeDeviceDockedHandler implements INodeMessageHandler<DeviceDockedResp>{
    @Override
    public boolean support(DeviceDockedResp message) {
        return message instanceof DeviceDockedResp;
    }

    @Override
    public void call(DeviceDockedResp message) {

    }
}
