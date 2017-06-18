package com.gopush.protocol.device;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;

/**
 * go-push
 *
 * @类功能说明：推送消息响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
public class PushResp extends DeviceMessageResp<PushResp> {

    private static final String DEVICE_KEY = "D";
    private final static String MSG_KEY = "ID";
    private final static String RESULT_KEY = "R";


    public enum Result{
        S,  //SUCCESS,
        D,  //DUPLICATE,
        NR, //NOT_REGISTERED,
        IN  //INTERNAL_ERROR
    }

    @Getter
    @JSONField(name = "D")
    private String device;

    @Getter
    @JSONField(name = "ID")
    private String msgId;

    @Getter
    @JSONField(name = "R")
    private Result result;

    @Override
    protected Type type() {
        return Type.PR;
    }

    @Override
    protected PushResp getThis() throws Exception {
        return this;
    }



}
