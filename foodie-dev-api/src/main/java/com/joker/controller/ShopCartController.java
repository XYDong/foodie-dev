package com.joker.controller;

import com.joker.pojo.bo.ShopcartBO;
import com.joker.utils.JSONResult;
import com.joker.utils.JsonUtils;
import com.joker.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
public class ShopCartController extends BaseController{

    private final RedisOperator redisOperator;

    public ShopCartController(RedisOperator redisOperator) {
        this.redisOperator = redisOperator;
    }

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("/add")
    public Object add(@RequestParam String userId, @RequestBody ShopcartBO shopcartBO,
                      HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("");
        }
        // 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        // 需要额外判断当前购物车中包含已经存在的商品，则累加商品数量
        String shopcartStr = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopcartBO> shopcarts = null;
        if (StringUtils.isNotBlank(shopcartStr)) {
            // redis 中已经有购物车了
            shopcarts = JsonUtils.jsonToList(shopcartStr, ShopcartBO.class);
            // 判断购物车种是否已存在改商品，如果有的话，counts累加
            boolean isHaving = false;
            for (ShopcartBO sc : shopcarts) {
                String specId = sc.getSpecId();
                if (specId.equals(shopcartBO.getSpecId())) {
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcarts.add(shopcartBO);
            }
        } else {
            // redis 中没有购物车
            shopcarts = new ArrayList<>();
            // 直接添加到购物车
            shopcarts.add(shopcartBO);
        }
        // 覆盖现有中redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId,JsonUtils.objectToJson(shopcarts));
        return JSONResult.ok();
    }
    @ApiOperation(value = "删除购物车中商品",notes = "删除购物车中商品",httpMethod = "POST")
    @PostMapping("/del")
    public Object del(@RequestParam String userId, @RequestParam String itemSpecId,
                      HttpServletRequest request, HttpServletResponse response){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JSONResult.errorMsg("参数不能为空");
        }
        //前端用户在删除购物车中的数据，再等了情况下，会同时在后端同步购物车到redis缓存
        String shopcartStr = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        if (StringUtils.isNotBlank(shopcartStr)) {
            // redis中已经有购物车了
            List<ShopcartBO> shopcartBOS = JsonUtils.jsonToList(shopcartStr, ShopcartBO.class);
            for (ShopcartBO sc : shopcartBOS) {
                String specId = sc.getSpecId();
                if (specId.equals(itemSpecId)) {
                    shopcartBOS.remove(sc);
                    break;
                }
            }
            // 覆盖现有redis中的购物车
            redisOperator.set(FOODIE_SHOPCART + ":" + userId,JsonUtils.objectToJson(shopcartBOS));
        }
        return JSONResult.ok();
    }

}
