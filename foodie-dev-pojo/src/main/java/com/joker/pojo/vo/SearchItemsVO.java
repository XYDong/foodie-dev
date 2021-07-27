package com.joker.pojo.vo;

import lombok.Data;

/**
 * 用于展示商品搜索列表结果的VO
 */
@Data
public class SearchItemsVO {
    private String itemId;
    private String itemName;
    private Integer sellCounts;
    private String imageUrl;
    private Integer price;
}
