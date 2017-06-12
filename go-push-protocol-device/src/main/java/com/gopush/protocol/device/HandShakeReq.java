package com.gopush.protocol.device;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：握手请求
 * @作者：chenxiangqi
 * @创建时间：2017/6/9
 * @VERSION：
 */
public class HandShakeReq extends DeviceMessageReq {

    protected static final JSONObject JSON_OBJECT = new JSONObject();
    @Override
    protected Type type() {
        return Type.HANDSHAKE_REQ;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return JSON_OBJECT;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {
        return;
    }
}
