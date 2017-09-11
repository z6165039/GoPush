package com.gopush.nodeserver.registers;

import com.alibaba.fastjson.JSON;
import com.gopush.common.utils.zk.ZkUtils;
import com.gopush.common.utils.zk.listener.ZkStateListener;
import com.gopush.infos.nodeserver.bo.NodeServerInfo;
import com.gopush.nodeserver.config.GoPushConfig;
import com.gopush.nodeserver.config.ZookeeperConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author 喝咖啡的囊地鼠
 * @date 2017/9/10 下午10:42
 */
@Slf4j
@Component
public class NodeServerRegisterService {

    private static final String NODE_SERVER_GROUP = "/NODE-SERVER";

    @Autowired
    private ZookeeperConfig zookeeperConfig;

    @Autowired
    private GoPushConfig goPushConfig;

    @PostConstruct
    public void init() {
        ZkUtils.instance().init(
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
        registerNodeServer();

    }

    @PreDestroy
    public void destory() {
        ZkUtils.instance().destory();
    }


    /**
     * 提交最新的数据
     *
     * @param data
     */
    public void postNewData(NodeServerInfo data) {
        ZkUtils.instance().setNodeData(
                "/NODE-SERVER/" + goPushConfig.getName(),
                JSON.toJSONString(data));
    }

    /**
     * 注册node-server服务
     */
    private void registerNodeServer() {

        if (!ZkUtils.instance().checkExists(NODE_SERVER_GROUP)) {
            boolean flag;
            do {
                flag = ZkUtils.instance().createNode(NODE_SERVER_GROUP, null, CreateMode.PERSISTENT);
            } while (!flag);
        }
        registerNodeInfo();
    }

    private void registerNodeInfo() {
        ZkUtils.instance().createNode(
                NODE_SERVER_GROUP + goPushConfig.getName(),
                null,
                CreateMode.EPHEMERAL);
    }


}
