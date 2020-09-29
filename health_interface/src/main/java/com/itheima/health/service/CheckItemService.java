package com.itheima.health.service;

import com.itheima.health.HealthException;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckItemService {
    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();

    /**
     *  添加检查项
     * @param checkitem
     */
    void add(CheckItem checkitem);

    /**
     * 条件分页查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckItem> findPage(QueryPageBean queryPageBean);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 修改检查项
     * @param checkitem
     */
    void update(CheckItem checkitem);

    /**
     * 通过id删除
     * Dubbo如果不声明，会使用RuntimeException包装我们的自定义异常
     * @param id
     */
    void deleteById(int id) throws HealthException;
}
