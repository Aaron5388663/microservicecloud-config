package com.atlp.spring.controller;

import com.atlp.spring.exception.UserNotExistException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
// 自定义异常处理器
public class WebMvcExceptionHandler {

//    @ResponseBody
//    @ExceptionHandler(UserNotExistException.class)
//    // 处理自定义的异常类并返回JSON数据
//    public Map<String, Object> handleException(Exception e) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", "user.notexist");
//        map.put("message", "用户出错了！");
//        return map;
//    }

    @ExceptionHandler(UserNotExistException.class)
    // 处理自定义的异常类自适应浏览器和客户端
    public String handleException(HttpServletRequest request, Exception e) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", "user.notexist");
        map.put("message", "用户出错了！");
        // 给默认处理错误类BasicErrorController传入一个错误码
        request.setAttribute("javax.servlet.error.status_code", 500);
        request.setAttribute("ext", map);
        return "forward:/error";
    }
}
