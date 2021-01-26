#### shiro
- Subject：大白话来讲就是用户（当然并不一定是用户，也可以指和当前应用交互的任何对象），我们在进行授权鉴权的所有操作都是围绕Subject（用户）展开的，在当前应用的任何地方都可以通过SecurityUtils的静态方法getSubject()轻松的拿到当前认证（登录）的用户。
- SecurityManager：安全管理器，Shiro中最核心的组件，它管理着当前应用中所有的安全操作，包括Subject（用户），我们围绕Subject展开的所有操作都需要与SecurityManager进行交互。可以理解为SpringMVC中的前端控制器。
- Realms：字面意思为领域，Shiro在进行权限操作时，需要从Realms中获取安全数据，也就是用户以及用户的角色和权限。配置Shiro，我们至少需要配置一个Realms，用于用户的认证和授权。通常我们的角色及权限信息都是存放在数据库中，所以Realms也可以算是一个权限相关的Dao层，SecurityManager在进行鉴权时会从Realms中获取权限信息。
#### 使用
1、引入依赖
```java
<dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.4.0</version>
        </dependency>
```
2、配置3个bean
- SecurityManager
```java
@Bean
    public SecurityManager securityManager(UserAuthorizingRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setRememberMeManager(null);
        return securityManager;
    }
```
- UserAuthorizingRealm 
- Shiro的过滤器工厂类shiroFilterFactoryBean，将上一步配置的安全管理器注入，并配置相应的过滤规则
```java
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
```
LinkedHashMap是为了保持顺序，Filter的配置顺序不能随便打乱，过滤器是按照我们配置的顺序来匹配的。范围大的过滤器要放在后面，/**这条如果放在前面，那么一来就匹配上了，就不会继续再往后走了
说明：
* authc：配置的url都必须认证通过才可以访问，它是Shiro内置的一个过滤器
对应的实现类 @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter

* anon：也是Shiro内置的，它对应的过滤器里面是空的，什么都没做，可以理解为不拦截
 对应的实现类 @see org.apache.shiro.web.filter.authc.AnonymousFilter  
 
3、实现两个方法
UserAuthorizingRealm extends AuthorizingRealm
- doGetAuthorizationInfo
- doGetAuthenticationInfo  
这样配置完成以后，就可以基于URL做粗粒度的权限控制了，我们可以通过不同的过滤器为URL配置不同的权限。 



