package com.gopush.protocol.node;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * go-push
 *
 * @类功能说明：Ping-Pong
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */

@Slf4j
public class Pong extends NodeMessageResp {

    protected static final JSONObject PONG = new JSONObject();

    @Override
    protected Type type() {
        return Type.PONG;
    }

    @Override
    protected JSONObject toEncode() throws JSONException {
        return PONG;
    }

    @Override
    protected void toDecode(JSONObject jsonObject) throws JSONException {
        return;
    }
}
