import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
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
        IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro_1.ini");
        SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken("kangkang", "123");
        subject.login(token);
        Assert.assertEquals(true, subject.isAuthenticated());
    }
}
