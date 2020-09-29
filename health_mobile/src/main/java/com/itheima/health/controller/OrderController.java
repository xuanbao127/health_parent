package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Order;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 预约提交
     * @param orderInfo
     * @return
     */
    @PostMapping("/submit")
    public Result submit(@RequestBody Map<String,String> orderInfo){
        // 验证校验
        //    验证前端提交过来的验证码与redis的验证码是否一致
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + orderInfo.get("telephone");
        //1. 先从redis中取出验证码codeInRedis key=手机号码
        Jedis jedis = jedisPool.getResource();
        String codeInRedis = jedis.get(key);
        //2. codeInRedis没值，提示用户重新获取验证码
        if(StringUtils.isEmpty(codeInRedis)){
            return new Result(false, "请重新获取验证码");
        }
        //3. codeInRedis有值, 则比较前端的验证码是否一致
        if(!codeInRedis.equals(orderInfo.get("validateCode"))) {
            //   不一样，则返回验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        // 防止重复提交, 移除验证码
        jedis.del(key);
        //   一样，则继续执行调用服务的方法预约
        // 设置预约类型 health_mobile给手机端微信公众号使用的，写死它的类型为微信预约
        orderInfo.put("orderType", Order.ORDERTYPE_WEIXIN);
        Order order = orderService.submit(orderInfo);
        //4. 返回订单信息给页面
        return new Result(true, MessageConstant.ORDER_SUCCESS,order);
    }

    /**
     * 订单详情
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Map<String,String> orderInfo = orderService.findById(id);
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS,orderInfo);
    }
}
