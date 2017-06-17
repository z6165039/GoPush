package com.gopush.protocol.node;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：节点运行信息响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class NodeInfoResp extends NodeMessageResp{


    @JSONField(name = "R")
    private int result;


    @Override
    protected Type type() {
        return Type.NIS;
    }

    @Override
    protected String toEncode() throws Exception {
        return JSON.toJSONString(this);
    }


}
