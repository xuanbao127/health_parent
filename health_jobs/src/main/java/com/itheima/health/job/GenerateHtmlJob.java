package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: No Description
 * User: Eric
 */
@Component
public class GenerateHtmlJob {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private Configuration freemarkerConfiguration;

    @Reference
    private SetmealService setmealService;

    @Value("${out_put_path}")
    private String out_put_path; // 保存的文件目录

    /**
     * init-method
     */
    @PostConstruct
    public void init(){
        // 模板目录
        freemarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(this.getClass(),"/ftl"));
        // 编码
        freemarkerConfiguration.setDefaultEncoding("utf-8");
    }

    /**
     * initialDelay 延迟多长时间后启动
     * fixedDelay: 间隔时间后执行
     */
    @Scheduled(initialDelay = 3000,fixedDelay = 60000)
    public void doGenerateHtml(){
        //1. 从redis获取要处理的套餐id集合
        Jedis jedis = jedisPool.getResource();
        String key = "setmeal:static:html";
        Set<String> setmealIds = jedis.smembers(key);
        //2. 有数据就要处理
        if(null != setmealIds && setmealIds.size() > 0) {
            //3. 取出所有的套餐id  id|操作符|时间戳
            for (String setmealId : setmealIds) {
                //4. 对value值按|切割
                String[] setmealInfo = setmealId.split("\\|");
                //5. 套餐的id, 查询套餐详情
                String sid = setmealInfo[0]; // 套餐的id
                String oper = setmealInfo[1]; // 操作符 0删除，1生成静态页面
                if("1".equals(oper)) {
                    Setmeal setmealDetail = setmealService.findDetailById(Integer.valueOf(sid));
                    //6. 设置完整图片路径
                    setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
                    //7. 创建数据模型
                    Map<String,Object> dataMap = new HashMap<String,Object>();
                    dataMap.put("setmeal",setmealDetail);
                    //8. 获取模板对象
                    try {
                        String filename = String.format("%s/setmeal_%d.html",out_put_path, setmealDetail.getId());
                        generateHtml("mobile_setmeal_detail.ftl", filename, dataMap);
                        //12. 删除对应的套餐的id
                        jedis.srem(key,setmealId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    // 删除文件
                    String filename = String.format("%s/setmeal_%s.html",out_put_path, sid);
                    new File(filename).delete();
                    //删除对应的套餐的id
                    jedis.srem(key,setmealId);
                }
            }
            // 生成列表页面
            generateSetmealList();
        }
    }

    /**
     * 生成套餐列表页面
     */
    private void generateSetmealList(){
        // 1.查询所有套餐信息
        List<Setmeal> setmealList = setmealService.findAll();
        // 2.设置套餐完整图片路径
        setmealList.forEach(setmeal -> {
            setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        });
        // 3.获取模板文件名
        String templateName = "mobile_setmeal.ftl";
        // 4. 生成 的文件名
        String filename = String.format("%s/mobile_setmeal.html",out_put_path);
        // 5. 构建数据模型
        Map<String,Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmealList",setmealList);
        // 6. 生成html
        generateHtml(templateName,filename,dataMap);
    }

    private void generateHtml(String templateName,String filename,Map<String,Object> dataMap ){
        try {
            Template template = freemarkerConfiguration.getTemplate(templateName);
            //9. 创建writer, 【注意】utf-8
            BufferedWriter writer =  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename),"utf-8"));
            //10. 填充模板
            template.process(dataMap,writer);
            //11. 关闭writer
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String filename = String.format("%s/setmeal_%d.html","C:/sz100/health_parent/health_mobile/src/main/webapp/pages", 12);
        System.out.println(filename);
    }
}
