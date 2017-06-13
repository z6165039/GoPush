package com.gopush.handler.device;


import lombok.Builder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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

    private static final int INT_ZERO = 0;
    private static final int INT_MAX_VAL = Integer.MAX_VALUE - 1;
    //队列允许的最大值
    private static final int MAX_QUEUE_NUM = INT_MAX_VAL;

    //接收消息的计数
    private AtomicInteger counter = new AtomicInteger(0);
    //失败处理的计数
    private AtomicInteger failCounter = new AtomicInteger(0);
    //重试处理的计数
    private AtomicInteger retryCounter = new AtomicInteger(0);

    //存储多少个子节点处理器
    private List<InteralProcessHandler> interalProcessHandlers = new ArrayList<>();

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
     * 消息队列里面超过这个大小就要进行告警
     */
    @Setter
    private int overNumWarn = 500;

    /**
     * 子处理器的个数
     */
    @Setter
    private int childHandlerCount = 1;

    private ScheduledExecutorService pool;


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


    /**
     * 批量处理的消息
     * @param list
     * @throws Exception
     */
    protected abstract void batchHandler(List<T> list) throws Exception;



    @PostConstruct
    public void init(){
        if (childHandlerCount <  0){
            throw new RuntimeException(getBatchExecutorName()+" inner child handler counter less or equal zero");
        }
        for (int i = 0 ; i< childHandlerCount; i++ ){
            InteralProcessHandler handler = new InteralProcessHandler(i)
        }
    }

    @PreDestroy
    public void destory(){
        interalProcessHandlers.stream().forEach( handler -> {
            handler.stop();
        });
    }

    protected void putMsg(T message){
        int count = counter.incrementAndGet();
        if(count >= INT_MAX_VAL ){
            counter.set(INT_ZERO);
        }
        InteralProcessHandler processHandler = interalProcessHandlers.get( count % childHandlerCount );
        processHandler.putMsg(message);
    }



    private class InteralProcessHandler {

        //存放消息缓存
        private Queue<T> queue = new ConcurrentLinkedQueue<>();

        //handler 索引
        private int index = 0;

        //是否内部创建的线程池
        private boolean inBuilder = true;

        //内部缓存的队列的大小
        private AtomicInteger count = new AtomicInteger(0);

        /**
         * 定时器线程池
         * 支持外部传入，与内部定义
         */
        private ScheduledExecutorService  pool;

        /**
         * 加入未传入线程池，则使用单线程的定时线程，可以设置核心线程数
         */
        private int corePoolSize = 1;

        public InteralProcessHandler(int index){
            this.index = index;
        }
        public InteralProcessHandler(int index,int corePoolSize){
            this(index);
            if (corePoolSize >= 1){
                this.corePoolSize = corePoolSize;
            }
        }
        public InteralProcessHandler(int index,ScheduledExecutorService pool){
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
            if (count.incrementAndGet() >= INT_MAX_VAL ){
                count.set(INT_ZERO);
            }
            queue.add(message);
        }

        private int getQueueSize(){
            return count.get();
        }

        //
        private void processInterval(){
            //不管三七二十一先处理一次
            do{
                if (queue.isEmpty()){
                    return;
                }
                //大于批量处理的请求
                if (count.get() > batchSize){
                    log.warn("{}-InteralProcessHandler-{} message queue size too long : size = {}",getBatchExecutorName(),index,count.get());
                }
                if(count.get() > overNumWarn){
                    log.info("{}-InteralProcessHandler-{} message queue size over warn num floor ! size = {} ",getBatchExecutorName(),index,count.get());
                    // TODO: 2017/6/13  进行告警处理
                }
                List<T> batchList = new ArrayList<>();
                while (batchList.size() < batchSize ){
                    try {
                        batchList.add(queue.remove());
                        count.decrementAndGet(); //减少
                    }catch (Exception e){
                        //重置计数值
                        count.set(queue.size());
                        log.error("{}-InteralProcessHandler-{} add message to processs list exception {}",getBatchExecutorName(),index,e);
                        break;
                    }
                }

                try {
                    batchHandler(batchList);
                }catch (Exception e){
                    log.error("Exception {}",e);
                    if(failCounter.incrementAndGet() >= INT_MAX_VAL){
                        failCounter.set(INT_ZERO);
                    }
                    //需要重试
                    if(retryFailure()){
                        if(retryCounter.incrementAndGet() >= INT_MAX_VAL){
                           retryCounter.set(INT_ZERO);
                        }
                        queue.addAll(batchList);
                        count.set(queue.size());
                    }
                }

            }while ( count.get() > batchSize );
        }


        /**
         * 子handler的启动
         */
        private void start(){
            if(pool == null){
                pool = Executors.newScheduledThreadPool(corePoolSize);
                inBuilder = true;
            }
            pool.scheduleWithFixedDelay(() -> {
                try {
                    processInterval();
                }catch (Exception e){
                    log.error("Exception {}",e);
                }
            },0,delay,TimeUnit.MILLISECONDS);

        }

        /**
         * 子handler 的关闭
         */
        public void stop(){
            if( pool != null ){
                if (inBuilder){
                    pool.shutdown();
                }else {
                    if(pool.isShutdown() || pool.isTerminated()){
                        return;
                    }else {
                        pool.shutdown();
                    }
                }
            }
        }




    }


}
