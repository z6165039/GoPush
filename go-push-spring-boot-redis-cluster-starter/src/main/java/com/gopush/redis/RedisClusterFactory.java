package com.gopush.redis;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Set;

/**
 * go-push
 *
 * @类功能说明：Redis-Cluster 工厂类
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/10 上午3:08
 * @VERSION：
 */

@NoArgsConstructor
@Builder
public class RedisClusterFactory {

    private static int clientPoolSize = 20;

    private static int clusterTimeout = 10000;

    private Set<HostAndPort> jedisClusterNodes;

    private Set<HostAndPort> dockedJedisClusterNodes;


    private JedisPoolConfig poolConfig;


    private JedisCluster jedisCluster;


    private JedisCluster dockedJedisCluster;

    private RedisClusterDefaultVisitor redisClusterDefaultVisitor;

    private BatchWriteRedisClusterPipelineVisitor batchWriteRedisClusterPipelineVisitor;

    private RedisClusterPipelineVisitor redisClusterPipelineVisitor;

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


    public synchronized RedisClusterPipelineVisitor buildPipelineRedisClusterVisitor() {
        if (null == redisClusterPipelineVisitor){
            JedisCluster t = getJedisCluster();
            JedisCluster m = getDockedJedisCluster();
            if (t == null){
                return null;
            }
            if (null == redisClusterPipelineVisitor){
                redisClusterPipelineVisitor = new RedisClusterPipelineVisitor(t,m);
            }
        }
        return redisClusterPipelineVisitor;

    }

    public synchronized RedisClusterDefaultVisitor buildDefaultRedisClusterVisitor(){
        if (null == redisClusterDefaultVisitor){
            JedisCluster t = getJedisCluster();
            JedisCluster m = getDockedJedisCluster();
            if (t == null){
                return null;
            }
            if (null == redisClusterDefaultVisitor){
                redisClusterDefaultVisitor = new RedisClusterDefaultVisitor(t,m);
            }
        }
        return redisClusterDefaultVisitor;
    }

    public synchronized BatchWriteRedisClusterPipelineVisitor buildBatchWritePipelineRedisClusterVisitor(){
        if (null == batchWriteRedisClusterPipelineVisitor ){
            JedisCluster t = getJedisCluster();
            JedisCluster m = getDockedJedisCluster();
            if (t == null){
                return null;
            }
            if (null == batchWriteRedisClusterPipelineVisitor){
                batchWriteRedisClusterPipelineVisitor = new BatchWriteRedisClusterPipelineVisitor(t,m);
            }
        }
        return batchWriteRedisClusterPipelineVisitor;
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
