package com.pomelo.controller;

import com.pomelo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.registry.infomodel.User;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.shiro.SecurityUtils.getSubject;

/**
 * @author：cp
 * @time：2021-2-4
 * @Description: todo
 */
//@RestController 此处不能用，否则返回前端页面为字符串无法被springmvc视图解析器解析跳转到jsp页面
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/obtainAllUsers")
    @ResponseBody
    public List<User> getAllUser() {
        return userService.findAllUser();
    }

    @RequestMapping(value = "security/login", method = {RequestMethod.POST})
    public ModelAndView authentication(@RequestParam("username") String userName, @RequestParam("password") String password,
                                       @RequestParam(value = "isRememberMe", required = false) Boolean isRememberMe,
                                       HttpServletRequest request, HttpServletResponse response) {

        if (isRememberMe == null) {
            isRememberMe = false;
        }
        request.getSession().setAttribute("abc", "def");
        //获取到Subject门面对象
        Subject subject = getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(userName, password);
        //先设置是否记住我
        usernamePasswordToken.setRememberMe(isRememberMe);
        SimpleCookie rememberCookie = new SimpleCookie("rememberKey");
        try {
            //将用户数据交给Shiro框架去做
            //你可以在自定义Realm中的认证方法doGetAuthenticationInfo()处打个断点
            subject.login(usernamePasswordToken);
            if (isRememberMe && userName.length() > 0) {
                //用于同一服务器内的cookie共享路径
                rememberCookie.setPath("/");
                rememberCookie.setMaxAge(Cookie.ONE_YEAR);
                rememberCookie.setHttpOnly(true);
                //存储用户名
                rememberCookie.setValue(userName);
                rememberCookie.saveTo(request, response);
            } else {
                rememberCookie.setPath("/");
                rememberCookie.removeFrom(request, response);
            }
        } catch (ExcessiveAttemptsException exception) {
            if (!subject.isAuthenticated()) {
                Map<String, String> failReason = new HashMap<>(1);
                failReason.put("msg", "登录次数已经超过限制，请一分钟后重试");
                //登录失败
                return new ModelAndView("fail", failReason);
            }
        } catch (AuthenticationException exception) {
            if (!subject.isAuthenticated()) {
                Map<String, String> failReason = new HashMap<>(1);
                failReason.put("msg", "帐号密码错误");
                //登录失败
                return new ModelAndView("fail", failReason);
            }
        }
        //登录成功
        return new ModelAndView("home");

    }

    @RequestMapping(value = "admin")
    public String enterAdmin() {
        System.out.println(getSubject().getSession().getAttribute("abc"));
        //跳转到 web-inf/pages/admin.jsp页面
        return "admin";
    }

    @RequestMapping(value = "login")
    public String login(HttpServletRequest request) {
        //获取客户本机cookie数据
        final Cookie rememberCookie = new SimpleCookie("rememberKey");
        rememberCookie.setPath("/");
        //从请求中获取加密cookie数据
        final String usernameFromCookie = rememberCookie.readValue(request, null);
        request.getSession().setAttribute("username", usernameFromCookie);
        //跳转到 login.jsp页面
        return "login";
    }
}
