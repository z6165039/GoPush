package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.devices.handlers.IDeviceMessageHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.protocol.device.Ping;
import com.gopush.protocol.device.Pong;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：PING请求批处理器
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:03
 * @VERSION：
 */

@Slf4j
public class PingHandler extends BatchProcesser<Object[]> implements IDeviceMessageHandler<Ping> {


    //响应
    private static final String PONG = Pong.builder().build().encode();


    @Override
    public boolean support(Ping message) {
        return message instanceof Ping;
    }

    @Override
    public void call(ChannelHandlerContext context, Ping message) {

        Channel channel = context.channel();
        if (!channel.hasAttr(Constants.CHANNEL_ATTR_HANDSHAKE)){
            log.warn("Channel has not handshake, channel {} will be closed",channel);
            context.close();
            return;
        }
        channel.writeAndFlush(PONG);

        putMsg(new Object[]{
                channel.attr(Constants.CHANNEL_ATTR_DEVICE).get(),
                channel.attr(Constants.CHANNEL_ATTR_IDLE).get()});

        log.debug("Receive ping! channel : {}, device : {}",channel, channel.attr(Constants.CHANNEL_ATTR_DEVICE).get());
    }


    @Override
    protected String getBatchExecutorName() {
        return "Ping-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return false;
    }


    /**
     * 设置设备在线的过期时间
     * @param batchReq
     * @throws Exception
     */
    @Override
    protected void batchHandler(List<Object[]> batchReq) throws Exception {

        // TODO: 2017/6/18  设置设备保活 在线
        //收到设备发送过来的 PING 请求

        log.debug("Process Ping completed!");
    }

}
