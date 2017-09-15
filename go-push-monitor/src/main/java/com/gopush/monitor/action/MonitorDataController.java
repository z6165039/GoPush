package com.gopush.monitor.action;

import com.gopush.infos.datacenter.bo.DataCenterInfo;
import com.gopush.infos.nodeserver.bo.NodeServerInfo;
import com.gopush.monitor.dymic.discovery.MonitorDataCenterService;
import com.gopush.monitor.dymic.discovery.MonitorNodeServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/15 下午5:09
 */
@RestController
@RequestMapping("/monitor")
public class MonitorDataController {

    @Autowired
    private MonitorDataCenterService monitorDataCenterService;

    @Autowired
    private MonitorNodeServerService monitorNodeServerService;

    @RequestMapping(value = "/dc",method = RequestMethod.GET)
    public List<DataCenterInfo> dataCenterInfos(){
        return monitorDataCenterService.dataCenterLoader();
    }

    @RequestMapping(value = "/node",method = RequestMethod.GET)
    public List<NodeServerInfo> nodeServceInfos(){
        return monitorNodeServerService.nodeServerLoader();
    }
}
