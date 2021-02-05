package com.pomelo.shiro.cache;

import com.pomelo.redis.RedisService;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author：cp
 * @time：2021-2-5
 * @Description: todo
 */
public class RedisCacheManager implements CacheManager {
    private static final Logger LOGGER = getLogger(RedisCacheManager.class);

    /**
     * Cache缓存时间，单位秒
     */
    private Long expireSeconds;

    /**
     * 使用ConcurrentHashMap作为键值对，可以适用于并发环境
     * key为缓存名
     * value为某个缓存对象
     */
    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<>();

    /**
     * 用于Cache的Redis key前缀
     */
    private String keyPrefix = "shiro_redis_cache:";

    private RedisService redisService;

    public RedisService getRedisService() {
        return redisService;
    }

    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }

    public Long getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(Long expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        if (LOGGER.isDebugEnabled()) {
           LOGGER.debug("获取名称为:{}的RedisCache实例.", name);
        }
        //获得cache
        Cache cache = caches.get(name);
        if (null == cache) {
            cache = new RedisCache<K, V>(redisService, keyPrefix + name, expireSeconds);
        }
        //加入caches集合
        caches.put(name, cache);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("创建新的Cache实例:{}.", name);
        }
        return cache;
    }
}
