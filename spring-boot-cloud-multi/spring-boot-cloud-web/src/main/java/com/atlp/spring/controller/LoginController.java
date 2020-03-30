package com.atlp.spring.controller;

import com.atlp.spring.exception.UserNotExistException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class LoginController {

    @PostMapping("/user/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Map<String, Object> map,
                        HttpSession session) {
        if(!StringUtils.isEmpty(username) && "123456".equals(password)) {
            session.setAttribute("loginUser", username);
            // 请求重定向、可以防止重复提交
            return "redirect:/main.html";
        } else if(!"admin".equals(username)) {
            throw new UserNotExistException();
        } else {
            map.put("msg", "用户名或密码错误，请重新输入！");
            return "login";
        }
    }
}
