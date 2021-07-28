package com.joker.controller;

import com.joker.pojo.bo.ShopcartBO;
import com.joker.service.ItemService;
import com.joker.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description 购物车
 * @CreateTime 2021年07月23日 13:57:00
 */
@RestController
@Api(value = "购物车接口controller", tags = {"购物车相关api"})
@RequestMapping("shopcart")
public class ShopCartController {

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public Object add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO,
                      HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return JSONResult.ok();
    }
    @ApiOperation(value = "删除购物车中商品",notes = "删除购物车中商品",httpMethod = "POST")
    @PostMapping("/del")
    public Object del(@RequestParam String userId, @RequestParam String itemSpecId,
                      HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        // TODO 前端用户在删除购物车中的数据，再等了情况下，会同时在后端同步购物车到redis缓存
        return JSONResult.ok();
    }

}
