package com.gopush.protocol.node;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：设备上线上报请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
public class DeviceDockedReq extends NodeMessageReq{
    @Override
    protected Type type() {
        return Type.DEVICE_DOCKED_REQ;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return null;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {

    }
}
