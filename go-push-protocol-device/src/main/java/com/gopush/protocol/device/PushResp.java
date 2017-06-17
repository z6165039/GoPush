package com.gopush.protocol.device;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：推送消息响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class PushResp extends DeviceMessageResp {

    private static final String DEVICE_KEY = "D";
    private final static String MSG_KEY = "ID";
    private final static String RESULT_KEY = "R";


    public enum Result{
        S,  //SUCCESS,
        D,  //DUPLICATE,
        NR, //NOT_REGISTERED,
        IN  //INTERNAL_ERROR
    }

    @JSONField(name = "D")
    private String device;

    @JSONField(name = "ID")
    private String msgId;

    @JSONField(name = "R")
    private Result result;

    @Override
    protected Type type() {
        return Type.PR;
    }

    @Override
    protected String toEncode() throws Exception {
        return JSON.toJSONString(this);
    }


}
