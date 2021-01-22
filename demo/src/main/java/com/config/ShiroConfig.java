package com.config;

import com.shiro.UserAuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.context.annotation.Configuration;

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
    @Bean
    public SecurityManager securityManager(UserAuthorizingRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 登录页面，无权限时跳转的路径
        shiroFilterFactoryBean.setLoginUrl("/login");
        // 配置拦截规则
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 首页配置放行
        filterMap.put("/", "anon");
        // 登录页面和登录请求路径需要放行
        filterMap.put("/login", "anon");
        filterMap.put("/do_login", "anon");
        // 其他未配置的所有路径都需要通过验证，否则跳转到登录页
        filterMap.put("/**", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

}
