package com.pomelo.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.xml.registry.infomodel.User;
import java.util.List;

/**
 * @author：cp
 * @time：2021-2-4
 * @Description: todo
 */
@Repository
public interface UserDao {
    List<User> findAllUser();

    String findPasswordByName(@Param("username") String username);
}
