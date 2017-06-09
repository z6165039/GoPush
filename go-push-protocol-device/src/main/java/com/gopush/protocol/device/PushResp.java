package com.gopush.protocol.device;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：推送消息响应
 * @作者：chenxiangqi
 * @创建时间：2017/6/9
 * @VERSION：
 */
public class PushResp extends DeviceMessageResp {
    @Override
    protected Type getType() {
        return Type.PUSH_RESP;
    }

    @Override
    protected JSONObject to() throws JSONException {
        return null;
    }

    @Override
    protected void from(JSONObject jsonObject) throws JSONException {

    }
}
