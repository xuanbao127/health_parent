package com.itheima.health.dao;

import com.itheima.health.pojo.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    /**
     * 添加订单
     * @param order
     */
    void add(Order order);

    /**
     * 预约订单查询
     * @param order
     * @return
     */
    List<Order> findByCondition(Order order);

    /**
     * 订单详情
     * @param id
     * @return
     */
    Map findById4Detail(Integer id);

    /**
     * 某个日期的预约数量
     * @param date
     * @return
     */
    Integer findOrderCountByDate(String date);

    /**
     * 某个期后的预约数量
     * @param date
     * @return
     */
    Integer findOrderCountAfterDate(String date);

    /**
     * 某个日期已到诊的数量
     * @param date
     * @return
     */
    Integer findVisitsCountByDate(String date);

    /**
     * 从某个日期后到诊数量
     * @param date
     * @return
     */
    Integer findVisitsCountAfterDate(String date);

    /**
     * 查询热门套餐
     * @return
     */
    List<Map<String,Object>> findHotSetmeal();

    /**
     * 统计日期范围内的预约数量
     * @param startDate
     * @param endDate
     * @return
     */
    int findOrderCountBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
