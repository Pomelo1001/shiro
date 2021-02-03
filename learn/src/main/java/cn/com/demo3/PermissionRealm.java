package cn.com.demo3;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.List;

/**
 * @author：cp
 * @time：2021-2-3
 * @Description: 一、授权的理解
 * 你是谁，你是谁决定了你的身份是什么，你的身份决定了你能干什么。
 * 这里牵扯出三种对象。
 * 用户对象user：当前操作的用户。
 * 角色对象role ：表示一组 "权限操作许可权" 的集合。
 * 权限对象permission：资源操作许可权。
 * 例如，大宇（user）需要下载（permission）一个高清无码的种子，需要VIP权限（role）。
 * 所以，本次下载大致流程是先判断大宇是不是vip，然后再查看vip这种角色有没有下载权限。
 */
public class PermissionRealm extends AuthorizingRealm {
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addRole(getYourRoleByUsernameFromDB(userName));
        info.addStringPermissions(getYourPermissionByUsernameFromDB(userName));
        return info;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String yourInputUsername = (String) token.getPrincipal();
        String yourInputPassword = new String((char[]) token.getCredentials());
        //默认要被验证的密码就是用户输入的密码，所以用户输入什么密码都是对的
        String passwordFromDB = yourInputPassword;
        return new SimpleAuthenticationInfo(yourInputUsername, passwordFromDB, getName());
    }

    private String getYourRoleByUsernameFromDB(String username) {
        return "coder";
    }

    private List<String> getYourPermissionByUsernameFromDB(String username) {
        return Arrays.asList("code:insert", "code:update");
    }
}
