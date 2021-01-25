package com.example.demo.service;

import com.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-22
 * @Description: todo
 */
@Service
public class LoginService {
    public void login(User user) {
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        token.setRememberMe(true);
        Subject subject = SecurityUtils.getSubject();
        subject.login(token);
    }
    public List<String> getAllPerms() {
        List<String> list = new ArrayList<>();
        list.add("user:list");
        list.add("user:info");
        list.add("user:save");
        list.add("user:update");
        list.add("user:delete");
        return list;
    }

    public List<String> getUserPerms(Integer id) {
        List<String> list = new ArrayList<>();
        list.add("user:list");
        list.add("user:info");
        return list;
    }

    public User getUserByUsername(String username) {
        return new User("zhangkuan", "zhangkuan");
    }
}
