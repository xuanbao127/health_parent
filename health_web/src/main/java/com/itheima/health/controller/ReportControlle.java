package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuanbao127 on 2020/9/28 9:01 下午
 */
@RestController
@RequestMapping("/report")
public class ReportControlle {

    @Reference
    private MemberService reportService;

    /**
     * 过去一年的每个月会员总数量
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        //构建过去1年，12个月的数据
        //日历
        Calendar car = Calendar.getInstance();
        //过去一年，对 年 ➖1
        car.add(Calendar.YEAR,-1);
        //遍历12次。构建12个月
        // 套餐名称集合
        List<String> months = new ArrayList<String>(12);
        //只需要年和月，所以先格式化
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i <12 ; i++) {
            //每次都要+一个月
            car.add(Calendar.MARCH,1);
            //只需要年和月
            Date data = car.getTime();
            // 日期格式  2020-01
            String monthStr = sdf.format(data);
            //把年月放入集合中
            months.add(monthStr);
        }
        // 调用服务查询
        List<Integer>memberCount =reportService.getMemberReport(months);
        //给前端返回一个map集合
        HashMap<String, Object> resluMap = new HashMap<>(2);
        resluMap.put("months",months);
        resluMap.put("memberCount",memberCount);
        //最后返回给前端
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resluMap) ;
    }

    public static void main(String[] args) {
        Calendar car = Calendar.getInstance();
        //过去一年，对 年 ➖1
        car.add(Calendar.YEAR,-1);
        System.out.println(car.getTime());

    }
}
