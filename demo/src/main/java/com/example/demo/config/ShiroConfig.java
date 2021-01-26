package com.example.demo.config;


import com.example.demo.shiro.AuthLoginFilter;
import com.example.demo.shiro.MySessionManager;
import com.example.demo.shiro.RedisSessionDao;
import com.example.demo.shiro.UserAuthorizingRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-22
 * @Description: todo
 */
@Configuration
public class ShiroConfig {

    @Value("${session.redis.expireTime}")
    private long expireTime;


    @Bean
    public SecurityManager securityManager(UserAuthorizingRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        // 取消Cookie中的RememberMe参数
        securityManager.setRememberMeManager(null);
        // 配置自定义Session管理器
        securityManager.setSessionManager(mySessionManager());
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登录，无权限是跳转的路径
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 登录成功后跳转的路径
        shiroFilterFactoryBean.setSuccessUrl("/info");
        // 错误页面，认证不通过跳转
        shiroFilterFactoryBean.setUnauthorizedUrl("/error");

        // 添加登录过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        // 这里注释的一行是我这次踩的一个小坑，我一开始按下面这么配置产生了一个我意料之外的问题
        // 详细分析见博客：https://www.guitu18.com/post/2020/01/06/64.html
        // filters.put("authLogin", authLoginFilter());
        // 正确的配置是需要我们自己new出来，不能将这个Filter交给Spring管理
        filters.put("authLogin", new AuthLoginFilter(500, "未登录或登录超时"));
        shiroFilterFactoryBean.setFilters(filters);

        // 配置拦截规则
        Map<String, String> filterChainMap = new LinkedHashMap<>();
        /**
         * authc：该过滤器下的页面必须验证后才能访问，它是Shiro内置的一个拦截器
         * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter
         * anon：它对应的过滤器里面是空的，什么都没做，可以理解为不拦截
         * @see org.apache.shiro.web.filter.authc.AnonymousFilter
         * authc：所有url都必须认证通过才可以访问；anon：所有url都都可以匿名访问
         */
        // 首页配置放行
        filterChainMap.put("/", "anon");
        filterChainMap.put("/index", "anon");
        // 登录页面和登录请求路径需要放行
        filterChainMap.put("/login", "anon");
        filterChainMap.put("/do_login", "anon");
        /**
         * "/do_logout"是退出方法，通常我们需要在退出时执行一些自定义操作
         * @see LoginController#doLogout()，如：记录日志资源回收等
         * 此处如果配置 filterChainMap.put("/do_logout", "logout");
         * 那么退出操作将会被Shiro接管，不会走到我们自定义的退出方法
         */
        //filterChainMap.put("/do_logout", "logout");
        // 未配置的所有路径都需要通过验证，否则跳转到登录页
        //filterChainMap.put("/**", "authc");
        // 使用自定义的登录过滤器
        filterChainMap.put("/api/login", "authLogin");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator lifecycleBeanPostProcessor() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 这里需要设置为True，否则 @RequiresPermissions 注解验证不生效
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public MySessionManager mySessionManager() {
        MySessionManager mySessionManager = new MySessionManager();
        // 配置自定义SessionDao
        mySessionManager.setSessionDAO(redisSessionDao());
        mySessionManager.setGlobalSessionTimeout(expireTime * 1000);
        return mySessionManager;
    }

    @Bean
    public RedisSessionDao redisSessionDao() {
        return new RedisSessionDao(expireTime);
    }


}
