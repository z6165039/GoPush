package com.gopush.datacenter.registers;

import com.gopush.common.constants.ZkGroupEnum;
import com.gopush.common.utils.zk.ZkUtils;
import com.gopush.common.utils.zk.listener.ZkStateListener;
import com.gopush.datacenter.config.GoPushDataCenterConfig;
import com.gopush.datacenter.config.ZookeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author chenxiangqi
 * @date 2017/9/12 下午2:20
 */

@Slf4j
@Component
public class DataCenterRegisterService {

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    @Autowired
    private GoPushDataCenterConfig goPushDataCenterConfig;

    private ZkUtils zkUtils;

    @PostConstruct
    public void init() {
        zkUtils = new ZkUtils();
        zkUtils.init(
                zookeeperConfig.getServers(),
                zookeeperConfig.getConnectionTimeout(),
                zookeeperConfig.getSessionTimeout(),
                zookeeperConfig.getMaxRetries(),
                zookeeperConfig.getRetriesSleepTime(),
                zookeeperConfig.getNamespace(),
                new ZkStateListener() {
                    @Override
                    public void connectedEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("链接zk成功");
                    }

                    @Override
                    public void ReconnectedEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("重新链接zk成功");
                    }

                    @Override
                    public void lostEvent(CuratorFramework curator, ConnectionState state) {
                        log.info("链接zk丢失");
                    }
                });
        registerDataCenter();

    }

    @PreDestroy
    public void destory() {
        zkUtils.destory();
    }



    /**
     * 注册datacenter服务
     */
    private void registerDataCenter() {

        if (!zkUtils.checkExists(ZkGroupEnum.DATA_CENTER.getValue())) {
            boolean flag;
            do {
                flag = zkUtils.createNode(ZkGroupEnum.DATA_CENTER.getValue(), null, CreateMode.PERSISTENT);
            } while (!flag);
        }
        registerDataCenterInfo();
    }

    private void registerDataCenterInfo() {
        zkUtils.createNode(
                ZKPaths.makePath(ZkGroupEnum.DATA_CENTER.getValue(),goPushDataCenterConfig.getName()),
                null,
                CreateMode.EPHEMERAL);
    }

}
