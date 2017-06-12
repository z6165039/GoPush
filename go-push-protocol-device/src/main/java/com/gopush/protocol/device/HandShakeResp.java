package com.gopush.protocol.device;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：握手响应
 * @作者：chenxiangqi
 * @创建时间：2017/6/9
 * @VERSION：
 */
public class HandShakeResp extends DeviceMessageResp {
    @Override
    protected Type type() {
        return Type.HANDSHAKE_RESP;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return null;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {
        //解码json 获取到分配的token 等
    }
}
