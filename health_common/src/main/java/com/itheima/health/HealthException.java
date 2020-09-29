package com.itheima.health;

import com.sun.xml.internal.ws.handler.HandlerException;

/**
 * Description:
 * 自定义异常使用场景：
 * 1. 区分系统异常还业务异常(自己抛出)
 * 2. 友好提示 自定义异常来包装系统异常
 * 3. 终止已经不符合业务逻辑代码的继续执行
 * User: Eric
 */
public class HealthException extends RuntimeException {
    public HealthException(String message){
        super(message);
    }
}
