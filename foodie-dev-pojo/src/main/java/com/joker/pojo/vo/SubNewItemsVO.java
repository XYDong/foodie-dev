package com.joker.pojo.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @version 1.0.0
 * @ClassName SubNewItemsVO.java
 * @Package com.joker.pojo.vo
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月27日 09:58:00
 */
@Data
@ApiModel(value = "最新商品信息VO",description = "最新商品信息VO")
public class SubNewItemsVO {
    private String itemId;
    private String itemName;
    private String itemUrl;
    private String createdTime;
}
