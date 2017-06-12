package com.gopush.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import java.lang.reflect.Field;

/**
 * go-push
 *
 * @类功能说明：
 * @作者：喝咖啡的囊地鼠
 * @创建时间：2017/6/10 下午2:10
 * @VERSION：
 */

@Slf4j
@AllArgsConstructor
public class JedisClusterPool {

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
                throw new RuntimeException("can not find or access field '" + fieldName + "' from " + cls.getName(), e);
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
                log.error("get jedis pool failure! key = {}, error = {}",key,e);
            }
            return null;

        }





}
