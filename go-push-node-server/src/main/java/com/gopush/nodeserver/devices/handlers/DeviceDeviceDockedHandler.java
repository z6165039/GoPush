package com.gopush.nodeserver.devices.handlers;

import com.gopush.common.Constants;
import com.gopush.common.utils.IpUtils;
import com.gopush.devices.handlers.IDeviceDockedHandler;
import com.gopush.nodeserver.devices.BatchProcesser;
import com.gopush.nodeserver.nodes.senders.INodeSender;
import com.gopush.protocol.node.DeviceDockedReq;
import com.gopush.redis.RedisClusterDefaultVisitor;
import com.gopush.springframework.boot.RedisClusterTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

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

    @Autowired
    private RedisClusterTemplate redisClusterTemplate;

    @Override
    public void upReport(String device, int channelHashCode, int[] idles) {
        putMsg(new Object[]{device,channelHashCode,idles});
        log.debug("up report device docked, device:{}, channelHashCode:{}, idles:{}",device,channelHashCode, Arrays.toString(idles));
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
        //添加缓存 设备-节点-channel 绑定
        if(CollectionUtils.isNotEmpty(batchReq)){
            RedisClusterDefaultVisitor visitor = redisClusterTemplate.defaultVisitor();
            String  nodeIp = IpUtils.intranetIp();
            DeviceDockedReq req = DeviceDockedReq.builder().node(nodeIp).build();
            batchReq.stream().forEach((ele) -> {
                req.addDevice((String)ele[0]);
                Map<String,String> hash = new HashMap<>();
                hash.put(Constants.DEVICE_CHANNEL_FIELD,String.valueOf(ele[1]));
                hash.put(Constants.DEVICE_NODE_FIELD,nodeIp);
                int[] idles = (int[]) ele[2];
                visitor.hmset(Constants.DEVICE_KEY + ele[0],hash,idles[0]);
            });
            //将需要上报的device 加到list 构造上报请求 使用 nodeSender 发送出去
            nodeSender.sendShuffle(req);
        }

    }
}
