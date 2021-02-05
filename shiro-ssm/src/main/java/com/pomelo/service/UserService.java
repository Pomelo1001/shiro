package com.pomelo.service;

import javax.xml.registry.infomodel.User;
import java.util.List;

/**
 * @author：cp
 * @time：2021-2-4
 * @Description: todo
 */
public interface UserService {
    List<User> findAllUser();

    String findPasswordByName(String username);
}
