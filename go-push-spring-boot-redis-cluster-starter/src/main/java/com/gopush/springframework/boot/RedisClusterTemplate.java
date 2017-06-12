package com.gopush.springframework.boot;

import com.gopush.redis.BatchWriteRedisClusterPipelineVisitor;
import com.gopush.redis.RedisClusterDefaultVisitor;
import com.gopush.redis.RedisClusterFactory;
import com.gopush.redis.RedisClusterPipelineVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * go-push
 *
 * @类功能说明：
 *              Redis-Cluster工具类,
 *                  提供给业务层自动注入
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/9 下午9:59
 * @VERSION：
 */
@AllArgsConstructor
@NoArgsConstructor
public class RedisClusterTemplate {


    @Setter
    @Getter
    private RedisClusterFactory redisClusterFactory;


    public RedisClusterDefaultVisitor defaultVisitor(){
        return redisClusterFactory.buildDefaultRedisClusterVisitor();
    }


    /**
     * pipeline还有问题,暂不提供
     */
//    public BatchWriteRedisClusterPipelineVisitor batchWritePipelineVisitor(){
//        return redisClusterFactory.buildBatchWritePipelineRedisClusterVisitor();
//    }

//    public RedisClusterPipelineVisitor pipelineVistor(){
//        return  redisClusterFactory.buildPipelineRedisClusterVisitor();
//    }


}
