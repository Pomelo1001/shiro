package com.pomelo.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author：cp
 * @time：2021-2-5
 * @Description: todo
 */
public class RetryLimitCredentialsMatcher extends HashedCredentialsMatcher {
    /**
     * 集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
     */
    private Cache<String, AtomicInteger> passwordRetryCache;
    private static final Logger LOGGER = getLogger(RetryLimitCredentialsMatcher.class);
    private static final String RETRY_CACHE_NAME = "passwordRetryCache";
    private static final Integer MAX_RETRY_COUNT = 3;

    /**
     * cacheManager对象由外部注入
     * 可以是Ehcache的CacheManager
     * 也可以注入自定义的CacheManager
     *
     * @param cacheManager cacheManager
     */
    private RetryLimitCredentialsMatcher(CacheManager cacheManager) {
        /**
         * 此处从CacheManager中获取缓存Cache对象
         * 本例中获取的缓存对象使用Ehcache.xml中获取
         * 如果是我们自定义CacheManager的话，
         * 可用下面的实现思路：
         * 先尝试从缓区池中获取名为RETRY_CACHE_NAME的缓存对象
         * 如果缓存池中没有名为RETRY_CACHE_NAME的缓存对象
         * 那么则创建名为RETRY_CACHE_NAME的缓存对象，并放入到缓存池中
         * 保证本类属性passwordRetryCache不为空
         */
        passwordRetryCache = cacheManager.getCache(RETRY_CACHE_NAME);
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        final String userName = (String) token.getPrincipal();
        //先查看系统中是否已有登陆次数缓存
        AtomicInteger retryCount = passwordRetryCache.get(userName);
        // 如果之前没有登录缓存，则创建一个登录次数缓存。
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
        }

        //登陆次数+1
        retryCount.incrementAndGet();
        if (retryCount.get() > MAX_RETRY_COUNT){
            LOGGER.error("登录次数超过限制");
            throw new ExcessiveAttemptsException("用户:" + userName + "登录次数已经超过限制");
        }
        //将其保存到缓存中
        passwordRetryCache.put(userName,retryCount);
        //debug
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("用户:{},尝试登录次数:{}", userName, retryCount.get());
        }
        //调用超类验证器，判断是否登录成功
        boolean isMatcher = super.doCredentialsMatch(token, info);
        //如果成功则清除缓存
        if (isMatcher) {
            passwordRetryCache.remove(userName);
        }
        return isMatcher;
    }
}
