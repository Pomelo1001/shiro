import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.apache.shiro.mgt.SecurityManager;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author：cp
 * @time：2021-2-2
 * @Description: 自定义Realm之继承AuthorizingRealm
 */
public class AuthencateTest {
    @Test
    public void test() {
        //读取配置文件，相当于在加载数据源
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");
        //SecurityManager 是Shiro内部的底层实现，几乎所有功能都由其实现
        SecurityManager sm = factory.getInstance();
        //SecurityUtils是一个工具，方便用户调用，它封装了SecurityManager
        SecurityUtils.setSecurityManager(sm);
        //生成一个SecurityManager的门面类，即Subject。
        Subject subject = SecurityUtils.getSubject();
        //封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken("jay", "123456");
        //Subject接收到的方法参数，最终将会传到SecurityManager中进行验证
        //将用户的数据token 最终传递到Realm中进行对比
        subject.login(token);
        //判断本帐号是否已经被认证
        Assert.assertEquals(true, subject.isAuthenticated());
    }
    @Test
    public void testPermissionRealm(){
        Subject subject = login("anyUserName", "anyPassWord");
        //使用断言判断用户是否已经登录
        Assert.assertTrue(subject.isAuthenticated());
        //---------登录结束------------
        //---------检查当前用户的角色信息------------
        System.out.println("check role:" + subject.hasRole("coder"));
        //---------如果当前用户有此角色，无返回值。若没有此权限，则抛 UnauthorizedException------------
        subject.checkRole("coder");
        //---------检查当前用户的权限信息------------
        System.out.println("check permission:" + subject.isPermitted("code:insert"));
        //---------如果当前用户有此权限，无返回值。若没有此权限，则抛 UnauthorizedException------------
        subject.checkPermissions("code:insert", "code:update");

    }
    private Subject login(String userName,String password){
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");
        //SecurityManager 是Shiro内部的底层实现，几乎所有功能都由其实现
        SecurityManager sm = factory.getInstance();
        //SecurityUtils是一个工具，方便用户调用，它封装了SecurityManager
        SecurityUtils.setSecurityManager(sm);
        //生成一个SecurityManager的门面类，即Subject。
        Subject subject = SecurityUtils.getSubject();
        //封装用户的数据
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
        //Subject接收到的方法参数，最终将会传到SecurityManager中进行验证
        //将用户的数据token 最终传递到Realm中进行对比
        //调用自定义Realm的doGetAuthenticationInfo方法进行认证操
        subject.login(token);
        //返回当前shiro环境的门面
        return subject;
    }
}
