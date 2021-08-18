package com.joker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@RestController
@ApiIgnore
public class HelloController {

    @GetMapping("/hello")
    public Object hello(){
        return "hello world!!!";
    }

    @GetMapping("/setSession")
    public Object setSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("userInfo", "new user");
        // 设置该 session 超时时间，单位秒
        session.setMaxInactiveInterval(3600);
        session.getAttribute("userInfo");
        // session.removeAttribute("userInfo");
        return "ok";
    }
}
