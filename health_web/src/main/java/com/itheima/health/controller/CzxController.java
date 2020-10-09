package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.CzxService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by xuanbao127 on 2020/10/9 3:33 下午
 */
@RestController
@RequestMapping("/czx")
public class CzxController {
    @Reference
    private CzxService czxService;

    /**
     * 新增角色
     * @param czx
     * @param permissionIds
     * @param checkdKeys
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Role czx, Integer[] permissionIds, Integer[]checkdKeys){
        //调用服务添加
        czxService.add(czx,permissionIds,checkdKeys);
        return new Result(true, "新增角色成功");
    }
}
