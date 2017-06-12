package com.gopush.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.*;

import java.io.Closeable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * go-push
 *
 * @类功能说明：
 *      Redis-Cluster Pipeline 访问工具
 *          主要用于批量操作,提高性能
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/10 上午1:37
 * @VERSION：
 */

@Slf4j
public class BatchWriteRedisClusterPipelineVisitor extends RedisClusterVisitorOperation {

    private static int maxQueueLength = 10000;

    private Queue<RedisCommand> commands;


    public BatchWriteRedisClusterPipelineVisitor(JedisCluster jedisCluster, JedisCluster dockedJedisCluster, int maxExpire) {
        super( jedisCluster, dockedJedisCluster, maxExpire);
        commands = new LinkedList<>();
    }

    public BatchWriteRedisClusterPipelineVisitor(JedisCluster jedisCluster) {
        super( jedisCluster);
        commands = new LinkedList<>();
    }

    public BatchWriteRedisClusterPipelineVisitor(JedisCluster jedisCluster, JedisCluster dockedJedisCluster) {
        super(jedisCluster, dockedJedisCluster);
        commands = new LinkedList<>();
    }



    /**
     * Redis命令
     * @param <T>
     */
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    class RedisCommand<T>{
        Protocol.Command cmd;
        String key;
        String value;
        int expire;
        Response<T> response;
        public RedisCommand(Protocol.Command cmd,String key,String value,int expire){
            this.cmd =cmd;
            this.key = key;
            this.value = value;
            this.expire = expire;
            this.response = null;
        }
    }


    /**
     * 重新置换最大值队列的长度
     * @param maxQueueLength
     */
    public void setMaxQueueLength(int maxQueueLength){
        BatchWriteRedisClusterPipelineVisitor.maxQueueLength = maxQueueLength;
    }

    /**
     * 设置
     * @param key
     * @param value
     * @param expire
     */
    public boolean set(String key,String value,int expire){
        putCommand( new RedisCommand<String>(
                            Protocol.Command.SETEX,
                            key,
                            value,
                            chooseExpirse(expire)
                ));
        if (willSync()){
            return syncBatchWrite();
        }
        return Boolean.FALSE;
    }


    /**
     * 删除
     * @param key
     * @return
     */
    public boolean del(String key){
        putCommand( new RedisCommand<Long>(
                Protocol.Command.DEL,
                key,
                null,
                0
        ));
        if (willSync()){
           return syncBatchWrite();
        }
        return Boolean.FALSE;
    }





    // 批量写删除操作
    public boolean syncBatchWrite(){
        Queue<RedisCommand> commandsImages = commands;
        commands = new LinkedList<>();
        if (CollectionUtils.isEmpty(commandsImages)){
            return true;
        }
        return new WriteBooleanTemplate(StringUtils.EMPTY, null, 0){

            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {

                RedisClusterPipelineExecutor pipelineExecutor = new RedisClusterPipelineExecutor(cluster);
                commandsImages.forEach( (x) -> {x.response = pipelineExecutor.executeCommand(x);});
                pipelineExecutor.sync();
                commandsImages.forEach( (x) -> {
                    try {
                        x.response.get();
                    }catch (Exception e){
                        //pipeline 执行异常使用普通方式
                        executeCommand(cluster,x);
                    }
                });
                return true;
            }
        }.call();
    }

    private void executeCommand(JedisCluster cluster,RedisCommand command){
        try {

            switch (command.cmd){
                case SETEX:
                    cluster.setex(command.key, command.expire,command.value);
                    break;
                case DEL:
                    cluster.del(command.key);
                    break;
                default:
                    throw new RuntimeException("Unsupported Redis Command!");
            }
        }catch (Exception e){
            log.error("execute redis command failure ! command = {} , error = {}",command, e);
        }
    }




    private void putCommand(RedisCommand redisCommand){
        commands.add(redisCommand);
    }

    private boolean willSync(){
        return commands.size() >= maxQueueLength;
    }







}


/**
 * Pipeline执行器
 */
@Slf4j
class RedisClusterPipelineExecutor{

    private JedisClusterPool jedisClusterPool;

    private Map<JedisPool,JedisPipelineUnit> jedisPipelineUnitMap = new HashMap<>();


    class JedisPipelineUnit{

        private Jedis jedis;

        @Getter
        private Pipeline pipeline;

        public JedisPipelineUnit(JedisPool jedisPool){
            this.jedis = jedisPool.getResource();
            this.pipeline = jedis.pipelined();
        }

        public void close(){
            closeQuietly(pipeline);
            closeQuietly(jedis);
            pipeline = null;
            jedis = null;
        }

        private void  closeQuietly(Closeable closeable){
            if (closeable != null){
                try {
                    closeable.close();
                } catch (Exception ignore) {

                }
            }
        }



    }


    public RedisClusterPipelineExecutor(JedisCluster cluster){
        jedisClusterPool = new JedisClusterPool(cluster);
    }


    public <T> Response<T> executeCommand(BatchWriteRedisClusterPipelineVisitor.RedisCommand<T> command){
       try {
           JedisPool pool = jedisClusterPool.getJedisPool(command.key);
           if (pool == null){
               return null;
           }
           JedisPipelineUnit jedisPipelineUnit = jedisPipelineUnitMap.get(pool);
           if (jedisPipelineUnit == null){
               jedisPipelineUnit = new JedisPipelineUnit(pool);
               jedisPipelineUnitMap.put(pool,jedisPipelineUnit);
           }
           Pipeline pipeline = jedisPipelineUnit.getPipeline();
           switch (command.cmd){
               case SETEX:
                   return (Response<T>) pipeline.setex(command.key, command.expire, command.value);
               case DEL:
                   return  (Response<T>) pipeline.del(command.key);
               default:
                   throw new RuntimeException("Unsupported redis command = [" + command.cmd + "]");

           }
       }catch (Throwable e){
           log.error("execute pipeline command failure! command = {}, error = {}",command,e);
       }
       return null;
    }


    public void sync(){
        jedisPipelineUnitMap.forEach((key,value) -> {
            try {
                Pipeline pipeline = value.getPipeline();
                pipeline.sync();
            } catch (Exception e) {
                log.error("pipeline sync failure! error = {} ", e);
            } finally {
                value.close();
            }
        });

        jedisPipelineUnitMap.clear();
    }


}
