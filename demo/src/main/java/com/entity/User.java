package com.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-22
 * @Description: todo
 */
@Data
public class User implements Serializable {

    /**
     * 系统最高管理员ID，拥有Root权限
     */
    public static final int SUPER_ADMIN = 0;

    private Integer id = 1;

    private String username;

    private String password;

    private Integer status = 0;

    private String sessionId;

    private Date lastLoginTime;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
