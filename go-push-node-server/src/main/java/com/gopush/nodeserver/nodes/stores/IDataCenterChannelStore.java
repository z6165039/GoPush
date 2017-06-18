package com.gopush.nodeserver.nodes.stores;

import io.netty.channel.Channel;

import java.util.List;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/19 上午1:10
 * @VERSION：
 */
public interface IDataCenterChannelStore {


    List<Channel> getAllChannels();



    boolean contains(String dcId);
    /**
     * 根据 DCID 获取Channel
     * @param dcId
     * @return
     */
    Channel getChannel(String dcId);

    /**
     * 根据DCID删除 channel
     * @param dcId
     */
    void removeChannel(String dcId);

    /**
     * 根据DCID移除channel,对比 channel存不存在
     * @param dcId
     * @param channel
     */
    void removeChannel(String dcId,Channel channel);


    /**
     * 清空channel
     */
    void clear();

    /**
     * 添加DC
     * @param dcId
     * @param channel
     */
    void addChannel(String dcId, Channel channel);


    /**
     * DC-channel 计数
     * @return
     */
    int count();

}
