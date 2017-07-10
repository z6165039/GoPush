package com.gopush.protocol.node;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Builder;
import lombok.Getter;

/**
 * go-push
 *
 * @类功能说明：节点运行信息响应
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Builder
@Getter
public class NodeInfoResp extends NodeMessageResp<NodeInfoResp>{


    @JSONField(name = "R")
    private int result;


    @Override
    protected Type type() {
        return Type.NIS;
    }

    @Override
    protected NodeInfoResp getThis() {
        return this;
    }


}
