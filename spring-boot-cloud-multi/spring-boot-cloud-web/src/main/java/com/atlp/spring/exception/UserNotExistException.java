package com.atlp.spring.exception;

// 自定义异常类
public class UserNotExistException extends RuntimeException {

    // 返回自定义异常输出信息
    public UserNotExistException() {
        super("用户不存在！");
    }
}
