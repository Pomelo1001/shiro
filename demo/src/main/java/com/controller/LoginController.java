package com.controller;

import com.entity.User;
import com.result.JsonResult;
import com.service.LoginService;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-25
 * @Description: todo
 */
@RestController
public class LoginController {
    private Logger log = Logger.getLogger(this.getClass());

    @Autowired
    private LoginService loginService;

    /**
     * 登录
     *
     * @return
     */
    @RequestMapping("do_login")
    public JsonResult doLogin(User user) {
        log.debug("---------- username = " + user.getUsername() + ", " + "password = " + user.getPassword() + " ----------");
        loginService.login(user);
        return JsonResult.ok("登录成功", user.getUsername());
    }

    @RequestMapping("do_logout")
    public JsonResult doLogout() {
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        subject.logout();
        log.debug("---------- 用户 [" + user.getUsername() + "] 退出登录");
        return JsonResult.ok("退出成功", user.getUsername());
    }

}
