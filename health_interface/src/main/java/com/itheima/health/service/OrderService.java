package com.itheima.health.service;

import com.itheima.health.HealthException;
import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface OrderService {

    /**
     * 提交预约
     * @param orderInfo
     * @return
     */
    Order submit(Map<String, String> orderInfo) throws HealthException;

    /**
     * 订单详情
     * @param id
     * @return
     */
    Map<String, String> findById(int id);
}
