package com.joker.controller;

import com.joker.pojo.Carousel;
import com.joker.pojo.Category;
import com.joker.pojo.vo.CategoryVO;
import com.joker.service.CarouselService;
import com.joker.service.CategoryService;
import com.joker.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

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
    private final CategoryService categoryService;

    public IndexController(CarouselService carouselService, CategoryService categoryService) {
        this.carouselService = carouselService;
        this.categoryService = categoryService;
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

    @ApiOperation(value = "获取商品分类（一级）",notes = "获取商品分类（一级）",httpMethod = "GET")
    @GetMapping("/category")
    public JSONResult category(){
        List<Category> categories = categoryService.queryAllRootLevelCat();
        if (categories == null) {
            return JSONResult.errorMsg("未查询到一级分类");
        }
        return JSONResult.ok(categories);
    }
    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类（二级）",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subcategory(
            @ApiParam(value = "一级分类id",name = "rootCatId",required = true)
            @PathVariable Integer rootCatId){
        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        if (list == null) {
            return JSONResult.errorMsg("未查询到一级分类");
        }
        return JSONResult.ok(list);
    }
}
