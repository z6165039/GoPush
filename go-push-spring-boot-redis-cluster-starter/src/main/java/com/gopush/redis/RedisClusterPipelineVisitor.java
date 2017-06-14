package com.gopush.redis;

import redis.clients.jedis.JedisCluster;


/**
 * go-push
 *
 * @类功能说明：基本的Redis-Cluster
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/10 下午2:22
 * @VERSION：
 */
public class RedisClusterPipelineVisitor extends RedisClusterVisitorOperation {

    public RedisClusterPipelineVisitor(JedisCluster jedisCluster, JedisCluster dockedJedisCluster, int maxExpire) {
        super(jedisCluster, dockedJedisCluster, maxExpire);
    }

    public RedisClusterPipelineVisitor(JedisCluster jedisCluster) {
        super(jedisCluster);
    }

    public RedisClusterPipelineVisitor(JedisCluster jedisCluster, JedisCluster dockedJedisCluster) {
        super(jedisCluster, dockedJedisCluster);
    }


    private JedisClusterPool getJedisClusterPool(JedisCluster cluster){
        return  new JedisClusterPool(cluster);

    }


}
