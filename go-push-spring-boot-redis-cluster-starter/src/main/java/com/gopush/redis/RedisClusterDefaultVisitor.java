package com.gopush.redis;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.SetUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.Set;

/**
 * go-push
 *
 * @类功能说明：默认Redis-Cluster工具访问
 * @作者：chenxiangqi
 * @创建时间：2017/6/10 上午12:33
 * @VERSION：
 */
@Slf4j
public class RedisClusterDefaultVisitor extends RedisClusterVisitorOperation {


    public RedisClusterDefaultVisitor(JedisCluster jedisCluster, JedisCluster dockedJedisCluster, int maxExpireTime) {
        super(jedisCluster, dockedJedisCluster, maxExpireTime);
    }

    public RedisClusterDefaultVisitor(JedisCluster jedisCluster) {
        super(jedisCluster);
    }

    public RedisClusterDefaultVisitor(JedisCluster jedisCluster, JedisCluster dockedJedisCluster) {
        super( jedisCluster, dockedJedisCluster);
    }


    /****
     * 设置
     * @param key
     * @param vaule
     * @param expire
     * @return
     */
    public boolean set(String key,String vaule, int expire){
        return new WriteBooleanTemplate(key,vaule,expire){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                cluster.setex(redisKey,expire,vaule);
                return true;
            }
        }.call();
    }

    /**
     * 查询
     * @param key
     * @param defaultValue
     * @return
     */
    public String get(String key,String defaultValue){
        return new ReadTemplate<String>(key,defaultValue){

            @Override
            public String read(JedisCluster cluster, String redisKey) {
                String v = cluster.get(redisKey);
                return v == null ? defaultValue : v;
            }
        }.call();
    }


    /**
     * 删除
     * @param key
     * @return
     */
    public boolean del(String key){
        return new DeleteTemplate(key){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                return cluster.del(redisKey) > 0;
            }
        }.call();
    }





    public long incrBy(String key,int increment,int expire,long defaultValue){
        return new WriteTemplate<Long>(key,null,expire,defaultValue){
            @Override
            public Long write(JedisCluster cluster, String redisKey, int expire) {
                long v = cluster.incrBy(redisKey,increment);
                cluster.expire(redisKey,expire);
                return v;
            }
        }.call();
    }



    public long sadd(String key, int expire, String... members){
        return new WriteTemplate<Long>(key,null,expire,-1L){
            @Override
            public Long write(JedisCluster cluster, String redisKey, int expire) {
                long r = cluster.sadd(redisKey,members);
                cluster.expire(redisKey,expire);
                return r;
            }
        }.call();
    }



    public Set<String> smembers(String key){
        return new ReadTemplate<Set<String>>(key, SetUtils.EMPTY_SET){
            @Override
            public Set<String> read(JedisCluster cluster, String redisKey) {
                return cluster.smembers(redisKey);
            }
        }.call();
    }



    public boolean hset(String key,String field, String value, int expire){
        return new WriteBooleanTemplate(key,value,expire){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                cluster.hset(redisKey,field,value);
                cluster.expire(redisKey,expire);
                return true;
            }
        }.call();
    }

    public boolean hmset(String key, Map<String,String> hash, int expire){
        return new WriteBooleanTemplate(key,null,expire){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                cluster.hmset(redisKey,hash);
                cluster.expire(redisKey,expire);
                return true;
            }
        }.call();
    }


    public String hget(String key,String field,String defaultValue){
        return  new ReadTemplate<String>(key, defaultValue){
            @Override
            public String read(JedisCluster cluster, String redisKey) {
                String v = cluster.hget(redisKey,field);
                return v == null ? defaultValue : v;
            }
        }.call();
    }



    public boolean hdel(String key, String field){
        return new DeleteTemplate(key){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                return cluster.hdel(redisKey,field) > 0;
            }
        }.call();
    }

    public long hincryBy(String key, String field, int increment, long defaultValue, int expire){
        return new WriteTemplate<Long>(key,String.valueOf(increment),expire,defaultValue){
            @Override
            public Long write(JedisCluster cluster, String redisKey, int expire) {
                long v = cluster.hincrBy(redisKey, field, increment);
                cluster.expire(redisKey,expire);
                return v;
            }
        }.call();
    }


    public boolean exists(String key){
        return new ReadTemplate<Boolean>(key,Boolean.FALSE){
            @Override
            public Boolean read(JedisCluster cluster, String redisKey) {
                return cluster.exists(redisKey);
            }
        }.call();
    }





}
