package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    // 订阅 treeCache
    ///dubbo/ 接口包名/provides/ 解析 ip:port 接口 方法
    // forPath("/dubbo/com.itheim..CheckItemService/providers") ip:port findAll

    /*Proxy.newProxyInstance(CheckItemController.class.getClassLoader(), new Class[]{CheckItemService.class}, new InvocationHandler() {
           @Override
           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
               // 连接服务提供方
               Socket socket = new Socket(ip,port);
               socket.getOutputStream().write("findByAll".getBytes());
               InputStream inputStream = socket.getInputStream();
               inputStream.read() 字符流 // 获取服务端响应的结果
               // 反序列化
               return list;
           }
       });*/
    @Reference
    private CheckItemService checkItemService;

    /**
     * 查询所有的检查项
     * @return
     */
    @GetMapping("/findAll")
    //相当于 @RequestMapping(value="/findAll");
    // Restful
    // @GetMapping, 查询数据 RequestMethod.GET
    // @PutMapping  修改数据 RequestMethod.PUT
    // @PostMapping  添加数据RequestMethod.POST
    // @DeleteMapping 删除数据 RequestMethod.DELETE
    public Result findAll(){

        // 调用服务查询所有
        List<CheckItem> list = checkItemService.findAll();
        // 把结果包装到Reuslt再返回，统一返回的格式
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
    }

    /**
     * 添加检查项
     */
    @PostMapping("/add")
    public Result add(@RequestBody CheckItem checkitem){
        // 调用服务添加检查项，不报错就是成功
        checkItemService.add(checkitem);
        // 返回操作的结果
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 条件分页查询
     * @param queryPageBean
     * @return
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务分页查询
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);
        // 返回结果
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务通过id查询检查项信息
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    /**
     * 修改检查项
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkitem){
        // 调用服务修改检查项，不报错就是成功
        checkItemService.update(checkitem);
        // 返回操作的结果
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 通过id删除
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用服务删除
        checkItemService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }
}
