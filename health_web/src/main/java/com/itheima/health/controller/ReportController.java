package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 统计过去1年每个月的会员总数量
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        // 构建过去1年，12个月的数据
        // 日历
        Calendar car = Calendar.getInstance();
        // 过去1年, 对年 减 1
        car.add(Calendar.YEAR,-1);
        // 构建12个月
        List<String> months = new ArrayList<String>(12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        for (int i = 0; i < 12; i++) {
            // 每次要加1个月
            car.add(Calendar.MONTH, 1);
            // 只需要年-月
            Date date = car.getTime();
            // 2020-01...
            String monthStr = sdf.format(date);
            months.add(monthStr);
        }
        // 调用服务来查询
        List<Integer> memberCount = memberService.getMemberReport(months);
        // {months,memberCount}
        Map<String,Object> reslutMap = new HashMap<String,Object>(2);
        reslutMap.put("months", months);
        reslutMap.put("memberCount",memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,reslutMap);
    }

    /**
     * 套餐预约占比
     * @return
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        // 统计套餐预约个数
        List<Map<String,Object>> reportData = setmealService.getSetmealReport();
        // 前端的数据格式
        // {
        //    flag:true,
        //    message:"",
        //    data:{
        //        setmealNames:List<String>,
        //        setmealCount:List<map<String,Object>>
        //    }
        //}
        Map<String,Object> resultMap = new HashMap<String,Object>();
        // 抽取套餐名称
        List<String> setmealNames = new ArrayList<String>();
        if(null != reportData && reportData.size() > 0){
            for (Map<String, Object> data : reportData) {
                // 提取套餐名
                setmealNames.add((String)data.get("name"));
            }
        }
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",reportData);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    /**
     * 运营数据统计
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String,Object> reportData = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,reportData);
    }

    /**
     * 导出 excel报表
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        // 获取报表数据
        Map<String, Object> reportData = reportService.getBusinessReportData();
        // 获取模板路径
        // getRealPath("/") = webapp
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        // 通过模板创建工作簿
        try(Workbook wk = new XSSFWorkbook(template);
            ServletOutputStream os = res.getOutputStream();
        ) {
            // 获取工作表
            Sheet sht = wk.getSheetAt(0);
            // 获取行与单元格，填数据
            // 日期
            sht.getRow(2).getCell(5).setCellValue((String) reportData.get("reportDate"));
            // ============ 会员数据 ============
            sht.getRow(4).getCell(5).setCellValue((int) reportData.get("todayNewMember"));
            sht.getRow(4).getCell(7).setCellValue((int) reportData.get("totalMember"));
            sht.getRow(5).getCell(5).setCellValue((int) reportData.get("thisWeekNewMember"));
            sht.getRow(5).getCell(7).setCellValue((int) reportData.get("thisMonthNewMember"));

            // ============= 预约到诊数据统计 ===============
            sht.getRow(7).getCell(5).setCellValue((int) reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((int) reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((int) reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((int) reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((int) reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((int) reportData.get("thisMonthVisitsNumber"));

            // ============= 热门套餐 ============
            int rowIndex = 12;
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>>)reportData.get("hotSetmeal");
            for (Map<String, Object> setmeal : hotSetmeal) {
                Row row = sht.getRow(rowIndex);
                row.getCell(4).setCellValue((String) setmeal.get("name"));
                row.getCell(5).setCellValue((Long)setmeal.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmeal.get("proportion");
                row.getCell(6).setCellValue(proportion.doubleValue());
                row.getCell(7).setCellValue((String) setmeal.get("remark"));
                rowIndex++;
            }
            // 设置内容体格式excel,
            res.setContentType("application/vnd.ms-excel");
            // 设置输出流的头信息，告诉浏览器，这是文件下载
            String filename = "运营数据统计.xlsx";
            filename = new String(filename.getBytes(),"ISO-8859-1");
            res.setHeader("Content-Disposition","attachment;filename=" + filename);
            // 把工作簿输出到输出流

            wk.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
