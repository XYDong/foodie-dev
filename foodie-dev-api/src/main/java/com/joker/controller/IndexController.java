package com.joker.controller;

import com.joker.pojo.Carousel;
import com.joker.pojo.Category;
import com.joker.pojo.vo.CategoryVO;
import com.joker.pojo.vo.NewItemsVO;
import com.joker.service.CarouselService;
import com.joker.service.CategoryService;
import com.joker.utils.JSONResult;
import com.joker.utils.JsonUtils;
import com.joker.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
    private final RedisOperator redisOperator;

    public IndexController(CarouselService carouselService, CategoryService categoryService, RedisOperator redisOperator) {
        this.carouselService = carouselService;
        this.categoryService = categoryService;
        this.redisOperator = redisOperator;
    }


    /**
     * 1. 后台运营系统，一旦广告（轮播图）发生改变，就可以删除缓存，然后重置
     * 2. 定时重置，比如每天凌晨三点重置
     * 3. 每个轮播图都有可能是一个广告，每个广告都会有一个过期时间，过期了，再重置
     */
    @ApiOperation(value = "获取首页轮播图列表",notes = "获取首页轮播图列表",httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel(@RequestParam int isShow){
        String carouselStr = redisOperator.get("carousel");
        List<Carousel> carousels;
        if (StringUtils.isBlank(carouselStr)) {
            carousels = carouselService.queryAll(isShow);
            redisOperator.set("carousel", JsonUtils.objectToJson(carousels));
        } else {
            carousels = JsonUtils.jsonToList(carouselStr, Carousel.class);
        }

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
        String categoriesStr = redisOperator.get("categories");
        List<Category> categories;
        if (StringUtils.isBlank(categoriesStr)) {
            categories = categoryService.queryAllRootLevelCat();
            redisOperator.set("categories",JsonUtils.objectToJson(categories));
        } else {
            categories = JsonUtils.jsonToList(categoriesStr,Category.class);
        }
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

        String subCatsStr = redisOperator.get("subCat:" + rootCatId);
        List<CategoryVO> subCats;
        if (StringUtils.isBlank(subCatsStr)) {
            subCats = categoryService.getSubCatList(rootCatId);
            redisOperator.set("subCat:"+rootCatId,JsonUtils.objectToJson(subCats));
        } else {
            subCats = JsonUtils.jsonToList(subCatsStr,CategoryVO.class);
        }
        if (subCats == null) {
            return JSONResult.errorMsg("未查询到一级分类");
        }
        return JSONResult.ok(subCats);
    }
    @ApiOperation(value = "查询每个一级分类下最新的6条商品数据",notes = "查询每个一级分类下最新的6条商品数据",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(value = "一级分类id",name = "rootCatId",required = true)
            @PathVariable Integer rootCatId){
        if (rootCatId == null) {
            return JSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        if (list == null) {
            return JSONResult.errorMsg("未查询到一级分类");
        }
        return JSONResult.ok(list);
    }
}
