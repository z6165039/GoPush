package com.gopush.nodeserver.devices.handlers;

import com.gopush.devices.handlers.IDeviceDockedHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.nodeserver.nodes.senders.INodeSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/19 上午12:58
 * @VERSION：
 */

@Slf4j
public class DeviceDeviceDockedHandler extends BatchProcesser<Object[]> implements IDeviceDockedHandler {



    @Autowired
    private INodeSender nodeSender;


    @Override
    public void upReport(String device, int channel, int[] idles) {
        putMsg(new Object[]{device,channel,idles});
        log.debug("up report device docked, device:{}, channel:{}, idles:{}",device,channel, Arrays.toString(idles));
    }

    @Override
    protected String getBatchExecutorName() {
        return "DeviceDocked-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return true;
    }

    @Override
    protected void batchHandler(List<Object[]> batchReq) throws Exception {
        //将需要上报的device 加到list 构造上报请求 使用 nodeSender 发送出去


        //nodeSender.send();
    }
}
