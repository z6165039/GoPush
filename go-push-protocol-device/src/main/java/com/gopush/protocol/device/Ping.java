package com.gopush.protocol.device;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong 心跳
 * @作者：chenxiangqi
 * @创建时间：2017/6/9
 * @VERSION：
 */
public class Ping extends DeviceMessageReq {

    private static final JSONObject JSON_OBJECT = new JSONObject();

    @Override
    protected Type getType() {
        return Type.PING;
    }

    @Override
    protected JSONObject to() throws JSONException {
        return JSON_OBJECT;
    }

    @Override
    protected void from(JSONObject jsonObject) throws JSONException {
        return;
    }
}
