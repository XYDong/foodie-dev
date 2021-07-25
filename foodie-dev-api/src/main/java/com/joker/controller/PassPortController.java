package com.joker.controller;

import com.joker.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("passport")
public class PassPortController {

    private final UserService userService;

    public PassPortController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("usernameIsExist")
    public HttpStatus usernameIsExist(@RequestParam String username){
        // 判断入参不能为空
        if (StringUtils.isNotBlank(username)) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        // 查找注册的用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        return isExist ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;


    }


}
