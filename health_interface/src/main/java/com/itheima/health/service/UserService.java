package com.itheima.health.service;

import com.itheima.health.pojo.User;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserService {
    /**
     * 获取用户的角色权限信息
     * @param username
     * @return
     */
    User findByUsername(String username);
}
