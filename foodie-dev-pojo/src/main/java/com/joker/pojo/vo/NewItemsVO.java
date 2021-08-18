package com.joker.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName NewItemsVO.java
 * @Package com.joker.pojo.vo
 * @Author Joker
 * @Description 最新商品推荐VO
 * @CreateTime 2021年07月27日 09:50:00
 */
@Data
@ApiModel(value = "最新商品推荐VO",description = "最新商品推荐VO")
public class NewItemsVO {

    @ApiModelProperty(value = "父菜单id",name = "rootCatId",notes = "商品父id")
    private Integer rootCatId;
    @ApiModelProperty(value = "父菜单名称",name = "rootCatName",notes = "父菜单名称")
    private String rootCatName;
    @ApiModelProperty(value = "父菜单标语",name = "slogan",notes = "父菜单标语")
    private String slogan;
    @ApiModelProperty(value = "菜单图片",name = "catImage",notes = "菜单图片")
    private String catImage;
    @ApiModelProperty(value = "菜单颜色",name = "bgColor",notes = "菜单颜色")
    private String bgColor;
    @ApiModelProperty(value = "商品列表",name = "simpleItemList",notes = "商品列表")
    private List<SubNewItemsVO> simpleItemList;

}
