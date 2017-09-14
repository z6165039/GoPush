package com.gopush.datacenter.nodes.manager.listener.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/12 下午11:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReloadNodesEvent {
    private String eventName;
}
