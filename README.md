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
#### 记住一点
[Shiro不会去维护用户、维护权限；这些需要我们自己去设计/提供；然后通过相应的接口注入给Shiro即可。](https://www.iteye.com/blog/jinnianshilongnian-2018936)

#### remember me功能
    （1）当用户没有退出且关闭了浏览器，再次打开网站则不需要再次登录。

    （2）当用户在登陆时勾选"记住我"，退出登录后，下次登录的时候，登录表单会记住上次的登录名。  
    
#### Shiro集成Redis实现分布式集群Session共享——使用Redis共享Session原理
     所有服务器的session信息都存储到了同一个Redis集群中，即所有的服务都将 Session 的信息存储到 Redis 集群中，无论是对 Session 的注销、更新都会同步到集群中，达到了 Session 共享的目的。

     Cookie 保存在客户端浏览器中，而 Session 保存在服务器上。客户端浏览器访问服务器的时候，服务器把客户端信息以某种形式记录在服务器上，这就是 Session。客户端浏览器再次访问时只需要从该 Session 中查找该客户的状态就可以了。

     在实际工作中我们建议使用外部的缓存设备(包括Redis)来共享 Session，避免单个服务器节点挂掉而影响服务，共享数据都会放到外部缓存容器中。
####  Shiro缓存机制
 缓存的工作机制是：先从缓存中读取数据，如果没有再从数据库读取实际数据，并把读取的数据存入缓存，下次再访问相同资源的时候，直接从缓存中获取，这样就可以缓解一些数据库的压力。

缓存有两个比较常用的概念：

TTL（Time To Live ）

存活期，即从缓存中创建时间点开始直到它到期的一个时间段（不管在这个时间段内有没有访问都将过期）

TTI（Time To Idle）

空闲期，即一个数据多久没被访问将从缓存中移除的时间。

在并发环境下，一般使用ConcurrentHashMap键值对来做缓存对象。 Shiro默认支持的缓存是ehcache缓存  
#### Shiro集成Redis实现分布式集群Cache共享(站在巨人的肩膀上)
- Shiro使用的是Token来封装用户登录的信息，另外一边，从数据库中查询出来的数据存放在"AuthenticationInfo"中，然后将token与info进行对比，对比一致的话说明用户登录成功。在登录成功后，为了缓解数据库的压力，可以将用户登录成功的info信息缓存下来。一般使用的是一组键值对来封装数据。因此，缓存的键值对可以理解为
- "用户主凭证" ---- "用户登录成功后的info"  

  为了实现Shiro的 认证、授权缓存，我们需要把这些缓存信息统一存放到一个地方进行管理，常见情况下存放到Redis服务器中  
- Shiro在认证与授权的流程中，首先会调用CacheManager的getCache()方法获取缓存对象。如果有缓存对象的话，那么将缓存对象中存放的用户info取出来，与用户的token进行对比。如果没有缓存，那么通过CacheManager对象创建一个新的Cache对象。
  
接着调用Cache.put("用户主凭证"，"用户登录成功后的info")，将登录凭证存放到这个新创建的缓存对象中。
  
再次访问相同的请求，Shiro则通过用户主凭证，取出缓存Cache<K，V>对象，调用Cache.get(key)方法，得到缓存的info对象  
####  Shiro限制密码重试次数限制
基本思路
不管是单机还是集群，我们都得把用户的登录次数记录下来，放到缓存里面。单机使用的是Ehcache缓存，集群使用的是Redis缓存。单机或集群对于缓存来说，只是CacheManager接口的实现方式不同。

        我们可以按照如下的思路来限制登录次数：

        先查看是否系统中是否已有登录次数缓存。缓存对象结构预期为："用户名--登录次数"。

        如果之前没有登录缓存，则创建一个登录次数缓存。

        将缓存记录的登录次数加1。

        如果缓存次数已经超过限制，则驳回本次登录请求。

        将缓存次数其保存到缓存中。

        验证用户本次输入的帐号密码，如果登录登录成功，则清除掉登录次数的缓存。

        代码只是思路的翻译。我们按照上述思路还编写代码。

        用户名可以从Shiro的token中获取，登录次数可以使用原子类AtomicInteger保证线程安全。    
   - 代码只是思路的翻译