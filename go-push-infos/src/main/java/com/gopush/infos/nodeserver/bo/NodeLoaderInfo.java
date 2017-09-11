package com.gopush.infos.nodeserver.bo;

import lombok.*;

import java.util.List;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/10 下午1:59
 */

@Builder
@Data
public class NodeLoaderInfo {
    private int onlineDeviceCounter;
    private int onlineDcCounter;
    private List<HandlerInfo> handlerInfos;
}
