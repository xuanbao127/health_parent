package com.itheima.health.service.impl;

import com.itheima.health.dao.CzxDao;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.CzxService;

/**
 * Created by xuanbao127 on 2020/10/9 4:19 下午
 */
public class CzxServiceImpl implements CzxService {

    /**
     * 新增角色
     * @param czx
     * @param permissionIds
     * @param checkdKeys
     */
    @Override
    public void add(Role czx, Integer[] permissionIds, Integer[] checkdKeys) {
        //添加菜单
        CzxDao.add(czx);
        //获取菜单id
        Integer czxId = czx.getId();
        //遍历选中的菜单ID
        //首先判断该角色是否添加成功
        if (null != permissionIds){
            //遍历
            for (Integer permissionId : permissionIds) {
                //添加菜单-检查组跟检查项的关系
                CzxDao.addCzxPermission(czxId,permissionId);
            }
        }
        if (null != checkdKeys){
            for (Integer menuId : checkdKeys) {
                CzxDao.addCzxMenu(czxId,menuId);
            }
        }

    }
}
