package com.gopush.handler.device;


import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
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

    private static final int MAX_QUEUE_NUM = Integer.MAX_VALUE - 1;

    //接收消息的计数
    private AtomicInteger counter = new AtomicInteger(0);

    //存储多少个子节点处理器
    private List<InterChildProcessHandler> interChildHandlerList = new ArrayList<>();


    /**
     * 批量处理的定时器延时
     */
    @Setter
    private int delay = 1;

    /**
     * 批量处理的大小
     */
    @Setter
    private int batchSize = 500;

    /**
     * 子处理器的个数
     */
    @Setter
    private int childHandlerCount = 1;


    /**
     * 获取定时器执行的name
     * @return
     */
    protected abstract String getBatchExecutorName();

    /**
     * 处理失败的是否重试
     * @return
     */
    protected abstract boolean retryFailure();



    @PostConstruct
    public void init(){
    }

    @PreDestroy
    public void destory(){

    }

    protected void putMsg(T message){
        int count = counter.incrementAndGet();
        if(count >= MAX_QUEUE_NUM ){
            counter.set(0);
        }
        InterChildProcessHandler processHandler = interChildHandlerList.get( count % childHandlerCount );
        processHandler.putMsg(message);
    }



    private class InterChildProcessHandler {

        //存放消息缓存
        private Queue<T> queue = new ConcurrentLinkedQueue<T>();

        //handler 索引
        private int index = 0;

        //是否内部创建的线程池
        private boolean inBuilder = true;

        /**
         * 定时器线程池
         * 支持外部传入，与内部定义
         */
        private ScheduledExecutorService  pool;

        /**
         * 加入未传入线程池，则使用单线程的定时线程，可以设置核心线程数
         */
        private int corePoolSize = 1;

        public InterChildProcessHandler(int index){
            this.index = index;
        }
        public InterChildProcessHandler(int index,int corePoolSize){
            this(index);
            if (corePoolSize >= 1){
                this.corePoolSize = corePoolSize;
            }
        }
        public InterChildProcessHandler(int index,ScheduledExecutorService pool){
            this(index);
            if(pool != null){
                this.pool = pool;
                this.inBuilder = false;
            }
        }

        /**
         * 加入message进入缓存队列
         * @param message
         */
        private void putMsg(T message){
            queue.add(message);
        }

        private void start(){
            if(pool == null){
                pool = Executors.newScheduledThreadPool(corePoolSize);
                inBuilder = true;
            }
            pool.scheduleWithFixedDelay(() -> {
                try {

                }catch (Exception e){
                }
            },0,delay,TimeUnit.MILLISECONDS);

        }




    }


}
