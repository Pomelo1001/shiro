import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author：cp
 * @time：2021-2-1
 * @Description: todo
 */
public class MyTest {
    @Test
    public void test1(){
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("kangkang", "123");
        subject.login(token);
        Assert.assertEquals(true, subject.isAuthenticated());
    }

    @Test
    public void testPassword(){
        String password = "123456";
        Md5Hash md5Hash = new Md5Hash(password);
        //打印结果：e10adc3949ba59abbe56e057f20f883e
        System.out.println(md5Hash);
    }

    /**
     * MD5加密时最好提供一个salt（盐），我们可以加一些只有系统知道的干扰数据
     */
    @Test
    public void testPassword2(){
        String password = "123456";
        String csdnName = "路飞";
        //第二个参数就是 盐salt
        Md5Hash md5Hash = new Md5Hash(password,csdnName);
        //打印结果：6e588f84bc14b24845f9d2859665e4e5
        System.out.println(md5Hash);
    }
    /**
     * 为了保险起见，进行循环加密。将加密后密文视为明文，用之前相同的加密手段，再循环加密3次
     */
    @Test
    public void testPassword3(){
        String password = "123456";
        String csdnName = "路飞";
        //第二个参数就是 盐salt
        Md5Hash md5Hash = new Md5Hash(password,csdnName,3);
        //打印结果：2743642881917372ff39ece7a2ac44a0
        System.out.println(md5Hash);
    }

    /**
     * 实际项目：插入用户密码的时候，需要先进行加密处理，再插入数据库的表。在验证用户密码的时候，再使用相同的加密算法计算用户输入的密码
     */
    @Test
    public void testPasswordRealm() {
        //1、获取SecurityManager工厂，此处使用Ini配置文件初始化SecurityManager
        Factory<SecurityManager> factory =
                new IniSecurityManagerFactory("classpath:shiro.ini");
        //2、得到SecurityManager实例 并绑定给SecurityUtils
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        //3、得到Subject及创建用户名/密码身份验证Token（即用户身份/凭证）
        Subject subject = SecurityUtils.getSubject();
        //验证密码123456是否能够登录成功
        UsernamePasswordToken token = new UsernamePasswordToken("anyString", "123456");
        try {
            //4、登录，即身份验证
            subject.login(token);
        } catch (AuthenticationException e) {
            //5、身份验证失败
            e.printStackTrace();
        }
        Assert.assertEquals(true, subject.isAuthenticated()); //断言用户已经登录
        //6、退出
        subject.logout();
    }

}
