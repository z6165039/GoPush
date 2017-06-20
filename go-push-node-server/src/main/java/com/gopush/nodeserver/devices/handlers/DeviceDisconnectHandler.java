package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.devices.handlers.IDeviceDisconnectHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.nodeserver.devices.stores.IDeviceChannelStore;
import io.netty.channel.Channel;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/19 上午12:25
 * @VERSION：
 */

@Slf4j
public class DeviceDisconnectHandler extends BatchProcesser<Object[]> implements IDeviceDisconnectHandler {


    @Autowired
    private IDeviceChannelStore deviceChannelStore;


    @Override
    public void channelClosed(Channel channel) {
        String device = channel.attr(Constants.CHANNEL_ATTR_DEVICE).get();
        if (StringUtils.isNotEmpty(device)){
            //移除设备-channel 映射
            deviceChannelStore.removeChannel(device,channel);
            putMsg(new Object[]{device,channel.hashCode()});
        }
        log.debug("Channel has closed ! channel :{}, device :{}",channel,device);
    }

    @Override
    protected String getBatchExecutorName() {
        return "DeviceDisconnect-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return false;
    }

    @Override
    protected void batchHandler(List<Object[]> batchReq) throws Exception {

        // TODO: 2017/6/19 移除redis 中的保存的 设备-节点-channel信息
        // 因为异步,要求 channel id 一样才能移除,防止 异步时间差删除了新建的Channel


        // TODO: 2017/6/19  当前向node 上报哪些被批量移除  其实也可以不报,那样的话 DeviceDisconnect 请求是不是可以移除

        log.debug("process DeviceDisconnect completed! size : {}",batchReq.size());
    }
}
