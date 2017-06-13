package com.gopush.handler.device;


import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
public abstract class BaseBatchProcessHandler<T>{

    private static final String INFO_SEPARATOR="\\|";
    private static final int INT_ZERO = 0;
    private static final int INT_MAX_VAL = Integer.MAX_VALUE - 1;

    //接收消息的计数
    private AtomicInteger receiveCounter = new AtomicInteger(0);
    //失败处理的计数
    private AtomicInteger failCounter = new AtomicInteger(0);
    //重试处理的计数
    private AtomicInteger retryCounter = new AtomicInteger(0);

    //存储handler下的处理器
    private List<InternalProcessor> internalProcessors = new ArrayList<>();

    /**
     * 批量处理的定时器延时
     */
    @Setter
    private int delay = 1000;

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
    private int processorNum = 1;

    /**
     * 不指定线程池的时候,指定初始化默认创建的线程池的大小
     */
    @Setter
    private int corePoolSize = 0;


    /**
     * 可以指定子handler使用的线程池
     */
    @Setter
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
        if (processorNum <  0){
            throw new RuntimeException(getBatchExecutorName()+"  processorNum <= 0 ");
        }
        for (int i = 0 ; i< processorNum; i++ ){
            InternalProcessor processor = new InternalProcessor(i);
            internalProcessors.add(processor);
        }
    }

    @PreDestroy
    public void destory(){
        internalProcessors.stream().forEach( processor -> {
            processor.stop();
        });
    }

    /**
     * 消息加到缓存里面
     * @param message
     */
    protected void putMsg(T message){
        int count = receiveCounter.incrementAndGet();
        if(count >= INT_MAX_VAL ){
            receiveCounter.set(INT_ZERO);
        }
        InternalProcessor processor = internalProcessors.get( count % processorNum );
        processor.putMsg(message);
    }

    /**
     * 获取handler的基础信息
     */
    protected String getHandlerInfo(){
        StringBuilder sb = new StringBuilder(getBatchExecutorName()).append("[ ");
        sb
                .append("receiveMCounter:")
                .append(receiveCounter.get())
                .append(INFO_SEPARATOR)
                .append("failCounter:")
                .append(failCounter.get())
                .append(INFO_SEPARATOR)
                .append("retryCounter:")
                .append(retryCounter.get());

        if(!internalProcessors.isEmpty()){
            sb.append("-> ")
                    .append(
                            internalProcessors.stream()
                                    .map(InternalProcessor::processorInfo)
                                    .collect(Collectors.joining(","))
                    );

        }
        sb.append(" ]");
        return sb.toString();

    }





    /**
     * 内部批处理handler
     */
    @NoArgsConstructor
    private class InternalProcessor {

        private static final int DEFAULT_CORE_POOL_SIZE = 1;

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
        private ScheduledExecutorService  cpool;

        /**
         * 处理器构造
         */
        private InternalProcessor(int index){
            this.index = index;
            if(pool != null){
                this.cpool = pool;
                this.inBuilder = false;
            }
            else{
                if(corePoolSize <= 0){
                    corePoolSize = DEFAULT_CORE_POOL_SIZE;
                    this.inBuilder = true;
                }
                this.cpool = Executors.newScheduledThreadPool(corePoolSize);
            }
        }

        /**
         * 加入message进入缓存队列
         * @param message
         */
        private void putMsg(T message){
            if (this.count.incrementAndGet() >= INT_MAX_VAL ){
                this.count.set(INT_ZERO);
            }
            this.queue.add(message);
        }


        /**
         * 处理器批处理方法
         */
        private void processInterval(){
            //不管三七二十一先处理一次
            do{
                if (this.queue.isEmpty()){
                    return;
                }
                //大于批量处理的请求
                if (this.count.get() > batchSize){
                    log.warn("{}-InternalProcessor-{} message queue size too long : size = {}",getBatchExecutorName(),this.index,this.count.get());
                }
                if(this.count.get() > overNumWarn){
                    log.info("{}-InternalProcessor-{} message queue size over warn num floor ! size = {} ",getBatchExecutorName(),index,this.count.get());
                    // TODO: 2017/6/13  进行告警处理
                }
                List<T> batchList = new ArrayList<>();
                while (batchList.size() < batchSize ){
                    try {
                        batchList.add(this.queue.remove());
                        this.count.decrementAndGet(); //减少
                    }catch (Exception e){
                        //重置计数值
                        this.count.set(this.queue.size());
                        log.error("{}-InternalProcessor-{} add message to processs list exception {}",getBatchExecutorName(),this.index,e);
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
                        this.queue.addAll(batchList);
                        this.count.set(queue.size());
                    }
                }

            }while ( this.count.get() > batchSize );
        }


        /**
         * 内部处理器启动
         */
        private void start(){
            if(this.cpool == null){
                this.cpool = Executors.newScheduledThreadPool(corePoolSize);
                inBuilder = true;
            }
            this.cpool.scheduleWithFixedDelay(() -> {
                try {
                    processInterval();
                }catch (Exception e){
                    log.error("Exception {}",e);
                }
            },0,delay,TimeUnit.MILLISECONDS);

        }

        /**
         * 内部处理器关闭
         */
        private void stop(){
            if( this.cpool != null ){
                if (this.inBuilder){
                    this.cpool.shutdown();
                    this.cpool = null;
                }else {
                    this.cpool = null;
                    if(pool.isShutdown() || pool.isTerminated()){
                        return;
                    }else {
                        pool.shutdown();
                    }
                }
            }
        }


        /**
         * 返回该处理器的基础信息
         * @return
         */
        private String processorInfo(){
            return new StringBuilder()
                    .append("index:")
                    .append(this.index)
                    .append(INFO_SEPARATOR)
                    .append("load:")
                    .append(this.count.get())
                    .toString();
        }




    }


}
