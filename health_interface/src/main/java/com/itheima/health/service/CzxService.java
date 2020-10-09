package com.itheima.health.service;

import com.itheima.health.pojo.Role;

/**
 * Created by xuanbao127 on 2020/10/9 3:57 下午
 */
public interface CzxService {
    /**
     * 新增角色
     * @param czx
     * @param permissionIds
     * @param checkdKeys
     */
    void add(Role czx, Integer[] permissionIds, Integer[] checkdKeys);
}
