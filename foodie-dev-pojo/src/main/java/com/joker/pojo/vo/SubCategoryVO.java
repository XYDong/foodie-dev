package com.joker.pojo.vo;

import lombok.Data;

/**
 * 菜单三级分类
 */
@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
