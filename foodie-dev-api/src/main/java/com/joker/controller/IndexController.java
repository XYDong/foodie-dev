package com.joker.controller;

import com.joker.pojo.Carousel;
import com.joker.service.CarouselService;
import com.joker.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@Api(value = "首页",tags = "首页相关接口")
@RestController
@RequestMapping("index")
public class IndexController {
    private final CarouselService carouselService;

    public IndexController(CarouselService carouselService) {
        this.carouselService = carouselService;
    }

    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel(@RequestParam int isShow){
        List<Carousel> carousels = carouselService.queryAll(isShow);
        if (carousels == null) {
            return JSONResult.errorMsg("未查询到轮播图");
        }
        return JSONResult.ok(carousels);
    }

    /**
     * 首页需求分析：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果光标移动到大分类，则加载其子分类内容，如果子分类已存在，则不需要加载（懒加载）
     */
}
