package com.gopush.protocol.node;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

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
public class MultiMessageToOneDeviceResp extends NodeMessageResp{
    @Override
    protected Type type() {
        return Type.MULTI_MSG_TO_ONE_DEVICE_RESP;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return null;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {

    }
}
