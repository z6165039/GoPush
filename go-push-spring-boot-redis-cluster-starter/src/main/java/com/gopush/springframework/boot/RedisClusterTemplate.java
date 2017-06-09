package com.gopush.springframework.boot;

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
 *              Redis-Cluster模板类,
 *                  提供给业务层自动注入
 * @作者：chenxiangqi
 * @创建时间：2017/6/9 下午9:59
 * @VERSION：
 */
@AllArgsConstructor
@NoArgsConstructor
public class RedisClusterTemplate {


    @Setter
    @Getter
    private RedisClusterFactory redisClusterFactory;


    public RedisClusterDefaultVisitor redisDefBuilder(String prefix){
        return redisClusterFactory.buildDefaultRedisClusterVisitor(prefix);
    }

    public RedisClusterPipelineVisitor redisPipelineBuilder(){
        return redisClusterFactory.buildPipelineRedisClusterVisitor();
    }


}
