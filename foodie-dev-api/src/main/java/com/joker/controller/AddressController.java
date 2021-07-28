package com.joker.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@RestController
@Api(value = "地址相关",tags = {"地址相关接口api"})
@RequestMapping("/address")
public class AddressController {

    /**
     * 地址需求：
     * 1. 查询用户所有收货地址
     * 2.新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */



    @GetMapping("/hello")
    public Object hello(){
        return "hello world!!!";
    }
}
