package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    private static final Logger log = LoggerFactory.getLogger(SetmealController.class);

    @Reference
    private SetmealService setmealService;

    /**
     * 上传图片
     * 【注意】此处方法的参数名必须与el-upload中的name的值要一致，大小写严格区分
     *  否则 imgFile=null
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        //- 获得原文件名
        String originalFilename = imgFile.getOriginalFilename();
        //- 截取原文件名来获取后缀名 .jpg
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        //- 使用UUID生成唯一文件名 + 后缀名
        String uniqueName = UUID.randomUUID().toString() + extension;
        try {
            //- 调用QiNiuUtils上传文件
            QiNiuUtils.uploadViaByte(imgFile.getBytes(),uniqueName);
            //- 响应结果给页面
            //  - 封装结果到map
            Map<String,String> resultMap = new HashMap<String,String>();
            //  - map有2个key
            //    - domain
            resultMap.put("domain",QiNiuUtils.DOMAIN);
            //    - imgName
            resultMap.put("imgName",uniqueName);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,resultMap);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("上传图片失败",e);
        }
        return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
    }

    /**
     * 添加套餐
     */
    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务添加套餐
        setmealService.add(setmeal,checkgroupIds);
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页条件查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 通过id查询套餐信息
     */
    @GetMapping("/findById")
    public Result findById(int id){
        Setmeal setmeal = setmealService.findById(id);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("setmeal",setmeal);
        // 解决页面中，图片回显问题，不管用户是否修改了图片，都不需要改变原有的代码
        resultMap.put("domain", QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    /**
     * 查询选中的检查组id集合
     */
    @GetMapping("/findCheckGroupIdsBySetmealId")
    public Result findCheckGroupIdsBySetmealId(int id){
        List<Integer> list = setmealService.findCheckGroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
    }

    /**
     * 更新套餐
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        // 调用服务更新套餐
        setmealService.update(setmeal,checkgroupIds);
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /**
     * 通过id删除套餐
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除套餐
        setmealService.deleteById(id);
        return new Result(true, "删除套餐成功!");
    }
}
