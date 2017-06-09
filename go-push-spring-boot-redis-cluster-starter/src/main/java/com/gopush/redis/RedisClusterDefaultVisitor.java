package com.gopush.redis;


import org.apache.commons.collections.SetUtils;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.Set;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：chenxiangqi
 * @创建时间：2017/6/10 上午12:33
 * @VERSION：
 */
public class RedisClusterDefaultVisitor extends RedisClusterVisitorOperation {


    public RedisClusterDefaultVisitor(String prefix, JedisCluster jedisCluster, JedisCluster dockedJedisCluster, int maxExpireTime) {
        super(prefix, jedisCluster, dockedJedisCluster, maxExpireTime);
    }

    public RedisClusterDefaultVisitor(String prefix, JedisCluster jedisCluster) {
        super(prefix, jedisCluster);
    }

    public RedisClusterDefaultVisitor(String prefix, JedisCluster jedisCluster, JedisCluster dockedJedisCluster) {
        super(prefix, jedisCluster, dockedJedisCluster);
    }

    public String get(String key,String defaultValue){
        return new ReadTemplate<String>(key,defaultValue){

            @Override
            public String read(JedisCluster cluster, String redisKey) {
                String v = cluster.get(redisKey);
                return v == null ? defaultValue : v;
            }
        }.run();
    }

    public boolean set(String key,String vaule, int expire){
        return new WriteTemplateBoolean(key,vaule,expire){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                cluster.setex(redisKey,expire,vaule);
                return true;
            }
        }.run();
    }

    public boolean delete(String key){
        return new DeleteTemplate(key){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                return cluster.del(redisKey) > 0;
            }
        }.run();
    }

    public long incrby(String key,int increment,int expire,long defaultValue){
        return new WriteTemplate<Long>(key,null,expire,defaultValue){
            @Override
            public Long write(JedisCluster cluster, String redisKey, int expire) {
                long v = cluster.incrBy(redisKey,increment);
                cluster.expire(redisKey,expire);
                return v;
            }
        }.run();
    }

    public String hget(String key,String field,String defaultValue){
        return  new ReadTemplate<String>(key, defaultValue){
            @Override
            public String read(JedisCluster cluster, String redisKey) {
                String v = cluster.hget(redisKey,field);
                return v == null ? defaultValue : v;
            }
        }.run();
    }

    public boolean hset(String key,String field, String value, int expire){
        return new WriteTemplateBoolean(key,value,expire){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                cluster.hset(redisKey,field,value);
                cluster.expire(redisKey,expire);
                return true;
            }
        }.run();
    }

    public boolean hmset(String key, Map<String,String> hash, int expire){
        return new WriteTemplateBoolean(key,null,expire){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                cluster.hmset(redisKey,hash);
                cluster.expire(redisKey,expire);
                return true;
            }
        }.run();
    }


    public boolean hdel(String key, String field){
        return new DeleteTemplate(key){
            @Override
            public Boolean write(JedisCluster cluster, String redisKey, int expire) {
                return cluster.hdel(redisKey,field) > 0;
            }
        }.run();
    }

    public long hincrby(String key, String field, int increment, long defaultValue, int expire){
        return new WriteTemplate<Long>(key,String.valueOf(increment),expire,defaultValue){

            @Override
            public Long write(JedisCluster cluster, String redisKey, int expire) {
                long v = cluster.hincrBy(redisKey, field, increment);
                cluster.expire(redisKey,expire);
                return v;
            }
        }.run();
    }

    public Set<String> smembers(String key){
        return new ReadTemplate<Set<String>>(key, SetUtils.EMPTY_SET){
            @Override
            public Set<String> read(JedisCluster cluster, String redisKey) {
                return cluster.smembers(redisKey);
            }
        }.run();
    }

    public long sadd(String key, int expire, String... members){
        return new WriteTemplate<Long>(key,null,expire,-1L){
            @Override
            public Long write(JedisCluster cluster, String redisKey, int expire) {
                long r = cluster.sadd(redisKey,members);
                cluster.expire(redisKey,expire);
                return r;
            }
        }.run();
    }

}
