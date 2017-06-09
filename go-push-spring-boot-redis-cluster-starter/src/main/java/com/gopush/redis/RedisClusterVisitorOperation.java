package com.gopush.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

/**
 * go-push
 *
 * @类功能说明：Redis-Cluster 操作基类
 * @作者：chenxiangqi
 * @创建时间：2017/6/9 下午11:26
 * @VERSION：
 */

@Slf4j
@AllArgsConstructor
public abstract class RedisClusterVisitorOperation {

    @Setter
    @Getter
    //reids key's prefix
    private String prefix;

    @Setter
    @Getter
    private JedisCluster jedisCluster;

    @Setter
    @Getter
    private JedisCluster dockedJedisCluster;


    @Getter
    @Setter
    private int maxExpireTime = 3600 * 24 * 30;



    public RedisClusterVisitorOperation(String prefix,JedisCluster jedisCluster){
        this.prefix = prefix;
        this.jedisCluster = jedisCluster;
    }
    public RedisClusterVisitorOperation(String prefix,JedisCluster jedisCluster,JedisCluster dockedJedisCluster){
        this(prefix,jedisCluster);
        this.dockedJedisCluster = dockedJedisCluster;
    }



    @AllArgsConstructor
    protected abstract class ReadTemplate<T> {
        private String key;
        private T defaultValue;

        public abstract T read(JedisCluster cluster,String redisKey);

        public T run(){
            String rk = prefix + key;
            T value = reading(getJedisCluster(),rk);
            if (value == null){
                value = reading(getDockedJedisCluster(),rk);
            }
            return value == null ? defaultValue : value;

        }

        private T reading(JedisCluster cluster,String redisKey){
            if( cluster != null ){
                try{
                    return read(cluster,redisKey);
                }catch (Exception e){
                    log.error("redis cluster read error: key = {}, error = {} ",redisKey,e);
                }
            }
            return null;
        }

    }


    @AllArgsConstructor
    protected abstract class WriteTemplate<T> {

        private String key;
        private String value;
        private int expire;
        private T defaultValue;


        public abstract T write(JedisCluster cluster,String redisKey,int expire);

        public T run(){
            String rk = prefix + key;
            int seconds = adjustExpireTime(expire);
            T value  = writing(getJedisCluster(),rk,seconds);
            writing(getDockedJedisCluster(),rk,seconds);
            return value == null ? defaultValue : value;

        }


        private T writing(JedisCluster cluster,String redisKey,int expire){
            if( cluster != null ){
                try{
                    return write(cluster,redisKey,expire);
                }catch (Exception e){
                    log.error("redis cluster write error: key = {}, expire={}, value = {}, error = {} ",redisKey,expire,value,e);
                }
            }
            return null;
        }

    }

    protected abstract class WriteTemplateBoolean extends WriteTemplate<Boolean>{

        public WriteTemplateBoolean(String key, String value, int expire) {
            super(key, value, expire, false);
        }
    }

    protected abstract class  DeleteTemplate extends WriteTemplateBoolean{

        public DeleteTemplate(String key) {
            super(key, StringUtils.EMPTY, 0);
        }
    }




    protected int adjustExpireTime(int seconds) {
        return Math.min(seconds, maxExpireTime);
    }




}
