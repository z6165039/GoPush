package com.gopush.infos.bo;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author chenxiangqi
 * @date 2017/9/10 下午1:59
 */

@Builder
@Data
public class NodeServerInfo {
    private int onlineDeviceCounter;
    private int onlineDcCounter;
    private List<HandlerInfo> handlerInfos;
}
