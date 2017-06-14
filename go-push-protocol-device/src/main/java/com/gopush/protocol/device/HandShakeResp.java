package com.gopush.protocol.device;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：握手响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
@Builder
public class HandShakeResp extends DeviceMessageResp {


    private static final String RESULT_KEY = "R";

    private int result;

    @Override
    protected Type type() {
        return Type.HSR;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        JSONObject object = new JSONObject();
        object.put(RESULT_KEY,result);
        return object;
    }

    @Override
    protected void toDecode(JSONObject json) throws JSONException {
        result = json.getInt(RESULT_KEY);
    }
}
