package com.gopush.protocol.device;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：推送消息请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class PushReq<R> extends DeviceMessageReq {

    private static final String DEVICE_KEY = "D";
    private static final String MSG_KEY = "ID";
    private static final String MSG_CONTEXT_KEY = "MC";

    private String id;

    private R msg;

    @Override
    protected Type type() {
        return Type.P;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(MSG_KEY,id);
        object.put(MSG_CONTEXT_KEY,msg);
        return object;
    }

    @Override
    protected void toDecode(JSONObject json) throws JSONException {
        id = json.getString(MSG_KEY);
        msg = (R) json.get(MSG_CONTEXT_KEY);
    }
}
