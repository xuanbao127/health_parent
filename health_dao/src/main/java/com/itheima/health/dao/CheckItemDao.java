package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckItemDao {
    /**
     * 查询所有
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 添加检查项
     * @param checkitem
     */
    void add(CheckItem checkitem);

    /**
     * 分页条件查询
     * @param queryString
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

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
     * 统计个数检查组与检查项的关系表 条件检查的id=id
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 通过id删除
     * @param id
     */
    void deleteById(int id);
}
