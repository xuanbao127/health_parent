package com.itheima.health.controller;

import com.itheima.health.HealthException;
import com.itheima.health.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description: 返回的json格式的数据
 * 捕获controller抛出的异常
 * User: Eric
 *
 * log日志的打印：
 * 1. info: 打印执行流程
 * 2. debug: 打印 关键数据 id 手机号码
 * 3. error: 打印异常信息
 *
 * 数据库日志log  aop around
 * 记录表中重要数据的变更,日志是记录到数据库表中
 * 比如：银行里的转账 A
 */
@RestControllerAdvice
public class HealthExceptionHandler {
    /**
     * log日志对象
     * HealthExceptionHandler 分类吧
     */
    private static final Logger log = LoggerFactory.getLogger(HealthExceptionHandler.class);

    // try catch catch(异常的类型)
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException e){
        log.error("违反业务逻辑",e);
        return new Result(false, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        log.error("发生未知异常",e);
        return new Result(false, "操作失败，发生未知异常，请联系管理员");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Result handleAccessDeniedException(AccessDeniedException e){
        log.error("没有权限",e);
        return new Result(false, "权限不足");
    }
}
