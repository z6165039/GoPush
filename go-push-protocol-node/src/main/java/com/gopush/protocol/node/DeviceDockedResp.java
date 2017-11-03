package com.gopush.protocol.node;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

/**
 * go-push
 *
 * @类功能说明：设备上线上报响应
 * @author：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceDockedResp extends NodeMessageResp<DeviceDockedResp> {


    @JSONField(name = "R")
    private int result;

    @Override
    protected Type type() {
        return Type.DOS;
    }

    @Override
    protected DeviceDockedResp getThis() {
        return this;
    }


}
