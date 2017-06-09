package com.gopush.redis;

import lombok.NoArgsConstructor;
import lombok.Setter;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：chenxiangqi
 * @创建时间：2017/6/10 上午3:08
 * @VERSION：
 */

@NoArgsConstructor
public class RedisClusterFactory {

    private static int clientPoolSize = 20;

    private static int clusterTimeout = 10000;

    @Setter
    private Set<HostAndPort> jedisClusterNodes;

    @Setter
    private Set<HostAndPort> dockedJedisClusterNodes;


    private JedisPoolConfig poolConfig;


    private JedisCluster jedisCluster;


    private JedisCluster dockedJedisCluster;

    public void setClientPoolSize(int clientPoolSize){
        if (clientPoolSize > 0){
            RedisClusterFactory.clientPoolSize = clientPoolSize;
        }
    }

    public void setClusterTimeout(int clusterTimeout){
        if (clusterTimeout > 0) {
            RedisClusterFactory.clusterTimeout = clusterTimeout;
        }
    }


    public RedisClusterDefaultVisitor buildDefaultRedisClusterVisitor(String prefix){
        JedisCluster t = getJedisCluster();
        JedisCluster m = getDockedJedisCluster();
        if (t == null){
            return null;
        }
        return new RedisClusterDefaultVisitor(prefix,t,m);
    }

    public RedisClusterPipelineVisitor buildPipelineRedisClusterVisitor(){
        JedisCluster t = getJedisCluster();
        JedisCluster m = getDockedJedisCluster();
        if (t == null){
            return null;
        }
        return new RedisClusterPipelineVisitor("",t,m);
    }

    private JedisPoolConfig getJedisPoolConfig() {
        if(poolConfig == null) {
            poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(clientPoolSize);
        }
        return poolConfig;
    }



    private JedisCluster getJedisCluster(){
        if (jedisClusterNodes == null){
            return null;
        }
        if (null == jedisCluster){
            synchronized (this){
                if (null == jedisCluster){
                    jedisCluster = new JedisCluster(jedisClusterNodes,clusterTimeout,getJedisPoolConfig());
                }
            }
        }
        return jedisCluster;
    }

    private JedisCluster getDockedJedisCluster(){
        if (dockedJedisClusterNodes == null){
            return null;
        }
        if (null == dockedJedisCluster){
            synchronized (this){
                if (null == dockedJedisCluster){
                    dockedJedisCluster = new JedisCluster(dockedJedisClusterNodes,clusterTimeout,getJedisPoolConfig());
                }
            }
        }
        return dockedJedisCluster;
    }

}
