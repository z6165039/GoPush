package com.gopush.protocol.device;

import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：握手请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
@Builder
public class HandShakeReq extends DeviceMessageReq {

    private static final String DEVICE_KEY = "D";
    private static final String DEVICE_TOKEN_KEY = "TK";

    private String device;

    private String token;


    @Override
    protected Type type() {
        return Type.HS;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(DEVICE_KEY,device);
        object.put(DEVICE_TOKEN_KEY,token);
        return object;
    }

    @Override
    protected void toDecode(JSONObject json) throws JSONException {
        device = json.getString(DEVICE_KEY);
        token = json.getString(DEVICE_TOKEN_KEY);
    }
}
