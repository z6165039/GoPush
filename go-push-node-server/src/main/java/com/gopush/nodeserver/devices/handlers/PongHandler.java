package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.devices.handlers.IDeviceMessageHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.protocol.device.DeviceMessage;
import com.gopush.protocol.device.Pong;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：PONG请求批处理器
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:03
 * @VERSION：
 */

@Builder
@Data
@Slf4j
public class PongHandler  extends BatchProcesser<Object[]> implements IDeviceMessageHandler<Pong> {

    @Override
    public boolean support(DeviceMessage message) {
        return message instanceof Pong;
    }

    @Override
    public void call(ChannelHandlerContext context, Pong message) {

        Channel channel = context.channel();
        if (!channel.hasAttr(Constants.CHANNEL_ATTR_HANDSHAKE)){
            log.warn("Channel has not handshake, channel :{} will be closed",channel);
            context.close();
            return;
        }
        putMsg(new Object[]{
                channel.attr(Constants.CHANNEL_ATTR_DEVICE).get(),
                channel.attr(Constants.CHANNEL_ATTR_IDLE).get()});
        log.debug("Receive PONG, channel : {}, device : {}",channel, channel.attr(Constants.CHANNEL_ATTR_DEVICE).get());

    }

    @Override
    protected String getBatchExecutorName() {
        return "Pong-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return false;
    }

    @Override
    protected void batchHandler(List<Object[]> batchReq) throws Exception {

        //发出去的PING请求的响应
        //也可以设置 保活设置

        // TODO: 2017/6/18 设置设备保活 在线

        log.debug("process Pong completed! size : {}",batchReq.size());
    }

}
