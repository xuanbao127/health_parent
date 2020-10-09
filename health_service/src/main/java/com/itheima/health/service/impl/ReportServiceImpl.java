package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sun.tools.jconsole.inspector.XObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 运营数据统计
     * @return
     */
    @Override
    public Map<String, Object> getBusinessReportData() {
        //reportDate 当天系统时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String reportDate = sdf.format(new Date());
        // 星期一
        String monday = sdf.format(DateUtils.getThisWeekMonday());
        // 星期天
        String sunday = sdf.format(DateUtils.getSundayOfThisWeek());
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDayOfThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());

        // ============== 会员统计 =====================
        //todayNewMember
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember >= 星期一
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);

        // ================= 预约到诊统计 ===================
        //todayOrderNumber
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber
        int todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday,sunday);
        //thisWeekVisitsNumber
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        // ============= 热门套餐 ============
        //hotSetmeal
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        Map<String,Object> reportData = new HashMap<String, Object>();
        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);
        return reportData;
    }
}
