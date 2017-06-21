package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.devices.handlers.IDeviceDockedHandler;
import com.gopush.devices.handlers.IDeviceMessageHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.protocol.device.DeviceMessage;
import com.gopush.protocol.device.HandShakeReq;
import com.gopush.protocol.device.HandShakeResp;
import com.gopush.springframework.boot.RedisClusterTemplate;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * go-push
 *
 * @类功能说明：握手请求批处理器
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:03
 * @VERSION：
 */

@Slf4j
public class HandShakeHandler extends BatchProcesser<Object[]> implements IDeviceMessageHandler<HandShakeReq> {


    @Autowired
    private RedisClusterTemplate redisClusterTemplate;

    @Autowired
    private IDeviceDockedHandler deviceDockedHandler;

    @Override
    public boolean support(HandShakeReq message) {
        return message instanceof HandShakeReq;
    }

    @Override
    public void call(ChannelHandlerContext context, HandShakeReq message) {
        putMsg(new Object[]{context.channel(),message});
        log.info("HandShakeRequest received! message :{}, channel :{}", message,context.channel());
    }


    @Override
    protected String getBatchExecutorName() {
        return "HandShake-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return false;
    }


    //握手成功
    public static final int HANDSAHKE_OK = 200;

    //非法设备
    public static final int HANDSHAKE_INVALID_DEVICE = 300;

    //非法token
    public static final int HANDSHAKE_INVALID_TOKEN = 301;

    @Override
    protected void batchHandler(List<Object[]> batchReq) throws Exception {
        if(CollectionUtils.isNotEmpty(batchReq)){
            //先全部取出redis 中存储的要处理的设备的列表
            batchReq.stream().forEach((e) ->{

                try {
                    Channel channel = (Channel) e[0];
                    HandShakeReq req = (HandShakeReq) e[1];
                    //建立 握手响应
                    HandShakeResp.HandShakeRespBuilder respBuilder =
                            HandShakeResp.builder();
                    if(StringUtils.isEmpty(req.getDevice())){
                        respBuilder.result(HANDSHAKE_INVALID_DEVICE);
                    }
                    else{
                        String token = redisClusterTemplate.defaultVisitor().hget(
                                Constants.R_DEVICE_TOKEN_KEY+req.getDevice(),
                                Constants.R_DEIVCE_TOKEN_FIELD,null);
                        //所有的token 都不为空 且 两个token相等
                        if(StringUtils.isAnyEmpty(token,req.getToken()) ||  !StringUtils.equals(req.getToken(),token)){
                            respBuilder.result(HANDSHAKE_INVALID_TOKEN);
                        }else{
                            respBuilder.result(HANDSAHKE_OK);
                        }
                    }
                    HandShakeResp resp = respBuilder.build();

                    String respEncode = resp.encode();
                    //握手不成功
                    if (resp.getResult() != HANDSAHKE_OK){
                        //将写出握手响应后关闭链接
                        channel.writeAndFlush(respEncode).addListener(ChannelFutureListener.CLOSE);
                        log.debug("Handshake fail, channel will be closed! channel :{}, device :{}, handshake response :{}",channel, req.getDevice(), respEncode);
                    }
                    else{

                        //将已经存在的链接关闭


                        //将握手结果, 设备信息  绑定到 通道属性里面
                        Integer[] idles  = new Integer[]{
                                req.getReadInterval() + Constants.READ_IDLE,
                                req.getWriteInterval() + Constants.WRITE_IDLE,
                                req.getAllInterval() + Constants.ALL_IDLE
                        };
                        channel.attr(Constants.CHANNEL_ATTR_IDLE).set(idles);
                        channel.attr(Constants.CHANNEL_ATTR_DEVICE).set(req.getDevice());
                        channel.attr(Constants.CHANNEL_ATTR_HANDSHAKE).set(Boolean.TRUE);

                        //重设读写超时器
                        channel.pipeline().replace("idleStateHandler","idleStateHandler",new IdleStateHandler(idles[0],idles[1],idles[2], TimeUnit.SECONDS));

                        //添加本地 设备-channel 绑定
                        //// TODO: 2017/6/18 添加本地 设备-channel 绑定


                        //添加缓存 设备-节点-channel 绑定
                        // TODO: 2017/6/18 添加缓存 设备-节点-channel 绑定

                        //报告设备上线
                        // TODO: 2017/6/18 报告设备上线
                        deviceDockedHandler.upReport(req.getDevice(),channel.hashCode(),new int[]{idles[0],idles[1],idles[2]});

                        //写出握手响应
                        channel.writeAndFlush(respEncode);
                        log.debug("HandShake successful, channel :{}, device :{}, send to device message :{}",channel,req.getDevice(),respEncode);


                    }
                }catch (Exception ex){
                    log.error("Exception HandShake , error :{}",ex);
                }

            });

            log.debug("Process Handshake request completed!");
        }

    }


}
