package com.gopush.protocol.node;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：节点运行信息请求
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9
 * @VERSION：
 */
@Slf4j
@Builder
public class NodeInfoReq extends NodeMessageReq{
    @Override
    protected Type type() {
        return Type.NI;
    }

    @Override
    protected String toEncode() throws Exception {
        return null;
    }


}
