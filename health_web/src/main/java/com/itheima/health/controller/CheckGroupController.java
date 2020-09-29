package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    /**
     * 添加检查组
     * checkitemIds 与 前端提交的参数名要一致
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        // 调用服务添加检查组
        checkGroupService.add(checkGroup,checkitemIds);
        // 响应结果
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务分页查询
        PageResult<CheckGroup> pageResult = checkGroupService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 查询检查组信息
        CheckGroup checkGroup = checkGroupService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
    }

    /**
     * 通过检查组id查询选中的检查项id集合
     */
    @GetMapping("/findCheckItemIdsByCheckGroupId")
    public Result findCheckItemIdsByCheckGroupId(int id){
        // 选中的检查项id
        List<Integer> checkitemIds = checkGroupService.findCheckItemIdsByCheckGroupId(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkitemIds);
    }

    /**
     * 修改
     * @param checkGroup
     * @param checkitemIds
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, Integer[] checkitemIds){
        // 调用服务修改检查组
        checkGroupService.update(checkGroup,checkitemIds);
        // 响应结果
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    /**
     * 通过id删除
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        checkGroupService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    /**
     * 查询所有
     */
    @GetMapping("/findAll")
    public Result findAll(){
        List<CheckGroup> list = checkGroupService.findAll();
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }
}
