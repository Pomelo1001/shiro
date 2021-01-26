package com.example.demo.shiro;

import com.example.demo.entity.User;
import com.example.demo.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-22
 * @Description: UserRealm，实现认证和授权
 */
@Component
public class UserAuthorizingRealm extends AuthorizingRealm {
    @Autowired
    private LoginService loginService;
    /**
     * 登录验证，获取身份信息
     *doGetAuthorizationInfo()：授权。认证过后，仅仅拥有登录权限，更多细粒度的权限控制，比如菜单权限，按钮权限，甚至方法调用权限等，
     * 都可以通过授权轻松实现。在这个方法里，我们可以拿到当前登录的用户，再根据实际业务赋予用户部分或全部权限，当然这里也可以赋予用户某些角色，后面也可以根据角色鉴权。
     * 下方的演示代码仅添加了权限，赋予角色可以调用addRoles()或者setRoles()方法，传入角色集合
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        // 获取用户
        User user = loginService.getUserByUsername(token.getUsername());
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        // 判断用户是否被锁定
        if (user.getStatus() == null || user.getStatus() == 1) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        // 验证密码
        if (!user.getPassword().equals(new String(token.getPassword()))) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        user.setSessionId(SecurityUtils.getSubject().getSession().getId().toString());
        // 设置最后登录时间
        user.setLastLoginTime(new Date());
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }

    /**
     * 授权验证，获取授权信息
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        User user = (User) principalCollection.getPrimaryPrincipal();
        List<String> perms;
        // 系统管理员拥有最高权限
        if (User.SUPER_ADMIN == user.getId()) {
            perms = loginService.getAllPerms();
        } else {
            perms = loginService.getUserPerms(user.getId());
        }

        // 权限Set集合
        Set<String> permsSet = new HashSet<>();
        for (String perm : perms) {
            permsSet.addAll(Arrays.asList(perm.trim().split(",")));
        }

        // 返回权限
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }
}
