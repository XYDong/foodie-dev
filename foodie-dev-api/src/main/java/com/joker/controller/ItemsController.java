package com.joker.controller;

import com.joker.pojo.*;
import com.joker.pojo.vo.*;
import com.joker.service.ItemService;
import com.joker.utils.JSONResult;
import com.joker.utils.PagedGridResult;
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
@Api(value = "商品",tags = "商品详情展示")
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController{
    private final ItemService itemService;

    public ItemsController(ItemService itemService) {
        this.itemService = itemService;
    }

    @ApiOperation(value = "获取商品详情",notes = "获取商品详情",httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemsImgs);
        itemInfoVO.setItemSpecList(itemsSpecs);
        itemInfoVO.setItemParams(itemsParam);
        return JSONResult.ok(itemInfoVO);
    }
    @ApiOperation(value = "获取商品详情",notes = "获取商品详情",httpMethod = "GET")
    @GetMapping("/info2/{itemId}")
    public JSONResult info2(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        Items item = itemService.queryItemById(itemId);
        if (item == null) {
            return JSONResult.errorMsg("未查询到商品详情");
        }
        return JSONResult.ok(item);
    }
    @ApiOperation(value = "获取商品详情图片列表",notes = "获取商品详情图片列表",httpMethod = "GET")
    @GetMapping("/images/{itemId}")
    public JSONResult images(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        List<ItemsImg> list = itemService.queryItemImgList(itemId);
        if (list == null) {
            return JSONResult.errorMsg("未查询到商品图片");
        }
        return JSONResult.ok(list);
    }

    @ApiOperation(value = "获取商品规格",notes = "获取商品规格",httpMethod = "GET")
    @GetMapping("/spec/{itemId}")
    public JSONResult spec(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        List<ItemsSpec> list = itemService.queryItemSpecList(itemId);
        if (list == null) {
            return JSONResult.errorMsg("未查询到轮商品规格");
        }
        return JSONResult.ok(list);
    }
    @ApiOperation(value = "获取商品参数",notes = "获取商品参数",httpMethod = "GET")
    @GetMapping("/param/{itemId}")
    public JSONResult param(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable String itemId){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        ItemsParam itemsParam = itemService.queryItemParam(itemId);
        if (itemsParam == null) {
            return JSONResult.errorMsg("未查询到商品参数");
        }
        return JSONResult.ok(itemsParam);
    }
    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级",httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);
        if (commentLevelCountsVO == null) {
            return JSONResult.errorMsg("未查询到商品评价等级");
        }
        return JSONResult.ok(commentLevelCountsVO);
    }

    @ApiOperation(value = "查询商品评价",notes = "查询商品评价",httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级")
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "页码", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示数量", required = true)
            @RequestParam Integer pageSize){
        if (itemId == null) {
            return JSONResult.errorMsg("商品id为空");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.queryItemComments(itemId, level, page, pageSize);
        if (pagedGridResult == null) {
            return JSONResult.errorMsg("未查询到商品评价");
        }
        return JSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "搜索商品列表",notes = "搜索商品列表  ",httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords", value = "关键词",required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序规则")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "页码", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示数量", required = true)
            @RequestParam Integer pageSize){
        if (StringUtils.isBlank(keywords)) {
            return JSONResult.errorMsg("请输入要查找的商品");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        if (pagedGridResult == null) {
            return JSONResult.errorMsg("未搜索到商品");
        }
        return JSONResult.ok(pagedGridResult);
    }
    @ApiOperation(value = "通过分类id搜索商品列表",notes = "搜索商品列表  ",httpMethod = "GET")
    @GetMapping("/catItems")
    public JSONResult catItems(
            @ApiParam(name = "catId", value = "三级分类id",required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序规则")
            @RequestParam String sort,
            @ApiParam(name = "page", value = "页码", required = true)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "每页展示数量", required = true)
            @RequestParam Integer pageSize){
        if (catId == null) {
            return JSONResult.errorMsg("请选择三级分类");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.searchItemsByThirdCat(sort,catId, page, pageSize);
        if (pagedGridResult == null) {
            return JSONResult.errorMsg("未搜索到商品");
        }
        return JSONResult.ok(pagedGridResult);
    }

    /**
     * 方法描述: <br>
     * <p> 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/28 15:42
     * @param itemSpecIds ids
     * @return Object
     * @ReviseName
     * @ReviseTime 2021/7/28 15:42
     **/
    @ApiOperation(value = "根据规格ids查询最新的购物车中商品数据",notes = "根据规格ids查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）",httpMethod = "POST")
    @PostMapping("/refresh")
    public JSONResult add(
            @ApiParam(name = "itemSpecIds",value = "拼接的规格ids",required = true,example = "1101,1002,1003")
            @RequestParam String itemSpecIds){
        if (StringUtils.isBlank(itemSpecIds)) {
            return JSONResult.ok();
        }
        List<ShopcartVO> shopcartVOS = itemService.queryItemsBySpecIds(itemSpecIds);
        return JSONResult.ok(shopcartVOS);
    }
}
