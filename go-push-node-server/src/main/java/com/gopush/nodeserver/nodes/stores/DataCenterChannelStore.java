package com.gopush.nodeserver.nodes.stores;

import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/19 上午1:10
 * @VERSION：
 */

@Slf4j
public class DataCenterChannelStore implements IDataCenterChannelStore {


    //计数器
    private AtomicInteger counter = new AtomicInteger(0);


    //DataCenter-channel列表
    private ConcurrentHashMap<String,Channel> dataCenterChannels = new ConcurrentHashMap<>();


    @Override
    public List<Channel> getAllChannels() {
        List<Channel> list = null;
        if(!dataCenterChannels.isEmpty()){
            list = new ArrayList<>();
            list.addAll(dataCenterChannels.values());
        }
        return list;
    }

    @Override
    public boolean contains(String dcId) {
        return dataCenterChannels.contains(dcId);
    }

    @Override
    public Channel getChannel(String dcId) {
        return dataCenterChannels.get(dcId);
    }

    @Override
    public void removeChannel(String dcId) {

        dataCenterChannels.remove(dcId);

        int count = counter.decrementAndGet();
        if (count < 0){
            counter.set(0);
        }
    }

    @Override
    public void removeChannel(String dcId, Channel channel) {
        if (channel.equals(dataCenterChannels.get(dcId))){
            removeChannel(dcId);
        }
    }

    @Override
    public void clear() {
        dataCenterChannels.clear();
        counter.set(0);
    }

    @Override
    public void addChannel(String dcId, Channel channel) {
        dataCenterChannels.put(dcId,channel);
        counter.incrementAndGet();
    }

    @Override
    public int count() {
        return counter.get();
    }
}



