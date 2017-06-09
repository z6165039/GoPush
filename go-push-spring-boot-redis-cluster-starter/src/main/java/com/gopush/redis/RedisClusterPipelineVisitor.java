package com.gopush.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：chenxiangqi
 * @创建时间：2017/6/10 上午1:37
 * @VERSION：
 */

@Slf4j
public class RedisClusterPipelineVisitor extends RedisClusterVisitorOperation {

    private static final int MAX_QUEUE_LENGTH = 10000;

    private Queue<RedisCommand> commands;


    public RedisClusterPipelineVisitor(String prefix, JedisCluster jedisCluster, JedisCluster dockedJedisCluster, int maxExpireTime) {
        super(prefix, jedisCluster, dockedJedisCluster, maxExpireTime);
        commands = new LinkedList<>();
    }

    public RedisClusterPipelineVisitor(String prefix, JedisCluster jedisCluster) {
        super(prefix, jedisCluster);
        commands = new LinkedList<>();
    }

    public RedisClusterPipelineVisitor(String prefix, JedisCluster jedisCluster, JedisCluster dockedJedisCluster) {
        super(prefix, jedisCluster, dockedJedisCluster);
        commands = new LinkedList<>();
    }


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



    public String set(String prefix,String key,String value,int expire){
        addCommand( new RedisCommand<String>(
                            Protocol.Command.SETEX,
                            prefix + key,
                            value,
                            adjustExpireTime(expire)
                ));
        if (shouldSync()){
            sync();
        }
        return "";
    }



    public long delete(String prefix,String key){
        addCommand( new RedisCommand<Long>(
                Protocol.Command.DEL,
                prefix + key,
                null,
                0
        ));
        if (shouldSync()){
            sync();
        }
        return 0;
    }


    public boolean sync(){
        Queue<RedisCommand> commandsImages = commands;
        commands = new LinkedList<>();
        if (CollectionUtils.isEmpty(commandsImages)){
            return true;
        }
        return new WriteTemplateBoolean(StringUtils.EMPTY, null, 0){

            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {

                RedisClusterPipelineExecutor pipelineExecutor = new RedisClusterPipelineExecutor(cluster);
                commandsImages.forEach( (x) -> {x.response = pipelineExecutor.executeCommand(x);});
                pipelineExecutor.sync();
                commandsImages.forEach( (x) -> {
                    try {
                        x.response.get();
                    }catch (Exception e){
                        executeCommand(cluster,x);
                    }
                });
                return true;
            }
        }.run();
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
            log.error("Execute redis command failure ! command = {} , error = {}",command, e);
        }
    }




    private void addCommand(RedisCommand redisCommand){
        commands.add(redisCommand);
    }

    private boolean shouldSync(){
        return commands.size() >= MAX_QUEUE_LENGTH;
    }







}


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


    public <T> Response<T> executeCommand(RedisClusterPipelineVisitor.RedisCommand<T> command){
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
           log.error("Execute pipeline command failure! command = {}, error = {}",command,e);
       }
       return null;
    }


    public void sync(){
        jedisPipelineUnitMap.forEach((key,value) -> {
            try {
                Pipeline pipeline = value.getPipeline();
                pipeline.sync();
            } catch (Exception e) {
                log.error("Pipeline sync failure! error = {} ", e);
            } finally {
                value.close();
            }
        });

        jedisPipelineUnitMap.clear();
    }


}

@Slf4j
@AllArgsConstructor
class JedisClusterPool{
    private static final Field FIELD_CONNECTION_HANDLER;
    private static final Field FIELD_CACHE;

    private JedisCluster cluster;

    static {
        FIELD_CONNECTION_HANDLER = getField(JedisCluster.class, "connectionHandler");
        FIELD_CACHE = getField(JedisClusterConnectionHandler.class, "cache");
    }

    private static Field getField(Class<?> cls,String fieldName){
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException | SecurityException e) {
            throw new RuntimeException("Can not find or access field '" + fieldName + "' from " + cls.getName(), e);
        }
    }


    private static <T> T getValue(Object obj,Field field) throws IllegalAccessException {
        return (T)field.get(obj);
    }


    public JedisPool getJedisPool(String key){

        try {
            JedisSlotBasedConnectionHandler handler = getValue(cluster,FIELD_CONNECTION_HANDLER);
            JedisClusterInfoCache clusterInfoCache = getValue(handler,FIELD_CACHE);
            int slot = JedisClusterCRC16.getSlot(key);
            return clusterInfoCache.getSlotPool(slot);
        } catch (IllegalAccessException e) {
            log.error("Get jedis pool failure! key = {}, error = {}",key,e);
        }
        return null;

    }



}