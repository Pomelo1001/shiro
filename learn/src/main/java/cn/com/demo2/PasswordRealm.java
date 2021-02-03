package cn.com.demo2;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @author：cp
 * @time：2021-2-2
 * @Description:
 * userName：用户输入的帐号
 *
 * getPasswordFromDB(clientUsername) ：通过用户输入的帐号，查询数据库，返回一串密文
 *
 * generateSalt(yourExpectedSalt)：你指定什么作为盐呢？这里的盐就是用户的名字，随便选取“路飞”
 *
 * getName()：当前Realm的名字。
 *
 * return new SimpleAuthenticationInfo(clientUsername, getPasswordFromDB(clientUsername),generateSalt(yourExpectedSalt), getName());
 */
public class PasswordRealm extends AuthorizingRealm {

    public PasswordRealm() {
        //采用md5算法
        HashedCredentialsMatcher passwordMatcher = new HashedCredentialsMatcher("md5");
        //循环加密3次
        passwordMatcher.setHashIterations(3);
        //再将这个加密组件注入到我们的Realm中
        this.setCredentialsMatcher(passwordMatcher);
    }

    @Override
    public String getName() {
        return "PasswordRealm";
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
        String yourExpectedSalt = "路飞";
        return new SimpleAuthenticationInfo(userName, getPasswordFromDB(userName), generateSalt(yourExpectedSalt), getName());

    }

    private String getPasswordFromDB(String clientUsername) {
        //模拟从数据库查出来的密文
        return "2743642881917372ff39ece7a2ac44a0";
    }

    private ByteSource generateSalt(String salt) {
        return ByteSource.Util.bytes(salt);
    }

}
