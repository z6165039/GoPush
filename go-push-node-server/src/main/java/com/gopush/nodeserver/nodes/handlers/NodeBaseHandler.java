package com.gopush.nodeserver.nodes.handlers;

import com.gopush.common.Constants;
import com.gopush.nodeserver.nodes.stores.IDataCenterChannelStore;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/22 上午1:34
 * @VERSION：
 */
public abstract class NodeBaseHandler {


    @Autowired
    private IDataCenterChannelStore dataCenterChannelStore;

    protected void saveLiveDc(Channel channel){
        if (!channel.hasAttr(Constants.CHANNEL_ATTR_DATACENTER)){
            //添加相应的值
            String dcId = new StringBuilder()
                    .append(channel.id())
                    .append(channel.remoteAddress().toString())
                    .toString();
            channel.attr(Constants.CHANNEL_ATTR_DATACENTER).set(dcId);
            if (!dataCenterChannelStore.contains(dcId)){
                dataCenterChannelStore.addChannel(dcId,channel);
            }
        }
    }

}
