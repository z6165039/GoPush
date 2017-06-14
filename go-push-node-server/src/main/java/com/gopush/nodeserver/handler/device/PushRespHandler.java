package com.gopush.nodeserver.handler.device;

import com.gopush.handler.device.BaseBatchProcessHandler;
import com.gopush.handler.device.DeviceMessageHandler;
import com.gopush.protocol.device.DeviceMessage;
import com.gopush.protocol.device.PushResp;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:08
 * @VERSION：
 */

@Builder
@Data
@Slf4j
public class PushRespHandler extends BaseBatchProcessHandler<PushRespHandler> implements DeviceMessageHandler {
    @Override
    public boolean support(DeviceMessage message) {
        return message instanceof PushResp;
    }

    @Override
    public void call(DeviceMessage message) {

    }

    @Override
    protected String getBatchExecutorName() {
        return "Resp-Push-BatchExecutor";
    }

    @Override
    protected boolean retryFailure() {
        return false;
    }

    @Override
    protected void batchHandler(List<PushRespHandler> list) throws Exception {

    }
}
