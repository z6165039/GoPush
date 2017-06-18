package com.gopush.protocol.node;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * go-push
 *
 * @类功能说明：设备下线上报请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */


@Builder
public class DeviceDisconReq extends NodeMessageReq<DeviceDisconReq>{



    //需要上报的设备列表(是批量上报的)
    @JSONField(name = "DEVS")
    private List<String> devices = new ArrayList<>();


    /**
     * 添加设备
     * @param device
     */

    public void addDevice(String device){
        if (!devices.contains(device)){
            devices.add(device);
        }
    }

    @Override
    protected Type type() {
        return Type.DI;
    }

    @Override
    protected DeviceDisconReq getThis() {
        return this;
    }

}
