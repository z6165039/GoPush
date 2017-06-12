package com.gopush.protocol.device;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong 心跳
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class Ping extends DeviceMessageReq {

    protected static final JSONObject PING = new JSONObject();
    @Override
    protected Type type() {
        return Type.PING;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return PING;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {
        return;
    }
}
