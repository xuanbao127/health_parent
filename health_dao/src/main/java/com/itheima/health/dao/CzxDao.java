package com.itheima.health.dao;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

/**
 * Created by xuanbao127 on 2020/10/9 4:35 下午
 */
public class CzxDao {
    /**
     * 新增
     * @param czx
     */
    public static void add(Role czx) {
    }

    public static void addCzxPermission(Integer czxId, Integer permissionId) {
    }

    public static void addCzxMenu(Integer czxId, Integer menuId) {
    }
}
