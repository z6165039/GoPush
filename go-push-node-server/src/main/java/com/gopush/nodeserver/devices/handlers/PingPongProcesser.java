package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.nodeserver.devices.BatchProcesser;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/22 上午1:28
 * @VERSION：
 */
@Slf4j
public  abstract class PingPongProcesser<T> extends BatchProcesser<T>{

    /**
     * 检测是否已经握手
     * @param channel
     * @return
     */
    protected boolean checkHandShake(Channel channel){
        if (!channel.hasAttr(Constants.CHANNEL_ATTR_HANDSHAKE)){
            log.warn("channel not handshake, channel:{}",channel);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
