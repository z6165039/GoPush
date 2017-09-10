package com.gopush.nodeserver.infos.bo;

import com.gopush.nodeserver.devices.infos.bo.HandlerInfo;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author chenxiangqi
 * @date 2017/9/10 下午1:59
 */

@Slf4j
@Builder
@Data
public class NodeServerInfo {
    private int onlineDeviceCounter;
    private int onlineDcCounter;
    private List<HandlerInfo> handlerInfos;
}
