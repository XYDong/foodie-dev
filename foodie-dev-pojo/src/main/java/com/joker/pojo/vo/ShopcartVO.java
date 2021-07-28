package com.joker.pojo.vo;

import lombok.Data;

/**
 * @version 1.0.0
 * @ClassName ShopcartBO.java
 * @Package com.joker.pojo.bo
 * @Author Joker
 * @Description 购物车
 * @CreateTime 2021年07月28日 14:46:00
 */
@Data
public class ShopcartVO {
    private String itemId;
    private String itemImgUrl;
    private String itemName;
    private String specId;
    private String specName;
    private String priceDiscount;
    private String priceNormal;
}
