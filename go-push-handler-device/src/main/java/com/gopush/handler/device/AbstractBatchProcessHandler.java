package com.gopush.handler.device;


import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/12 下午10:14
 * @VERSION：
 */


@Slf4j
@Builder
public abstract class AbstractBatchProcessHandler<T>{

    private final static int maxQueueNum = 10000000;

    @Setter
    private int delay = 1000;

    @Setter
    private int batchSize = 500;

    protected abstract String getBatchTimerName();

    protected abstract boolean retryFailure();

    private AtomicInteger counter = new AtomicInteger(0);

    private List<>

    @PostConstruct
    public void init(){

    }

    @PreDestroy
    public void destory(){

    }


    private class InterChildHandler {

        private int index = 0;

        private Queue<T> queue = new ConcurrentLinkedQueue<T>();




    }


}
