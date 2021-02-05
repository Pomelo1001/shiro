package com.pomelo.service.impl;

import com.pomelo.dao.UserDao;
import com.pomelo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.registry.infomodel.User;
import java.util.List;

/**
 * @author：cp
 * @time：2021-2-4
 * @Description: todo
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<User> findAllUser() {
        return userDao.findAllUser();
    }

    @Override
    public String findPasswordByName(String username) {
        return userDao.findPasswordByName(username);
    }
}
