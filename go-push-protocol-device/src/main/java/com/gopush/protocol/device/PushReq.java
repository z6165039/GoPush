package com.gopush.protocol.device;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：推送消息请求
 * @author：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PushReq extends DeviceMessageReq<PushReq> {

    @JSONField(name = "ID")
    private String id;

    @JSONField(name = "MCS")
    private List<String> msgs;

    @Override
    protected Type type() {
        return Type.P;
    }

    @Override
    protected PushReq getThis() throws Exception {
        return this;
    }


}
