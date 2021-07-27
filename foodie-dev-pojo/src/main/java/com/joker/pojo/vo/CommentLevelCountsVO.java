package com.joker.pojo.vo;

import lombok.Data;

/**
 * @version 1.0.0
 * @ClassName CommentLevelCountsVO.java
 * @Package com.joker.pojo.vo
 * @Author Joker
 * @Description 用于展示商品评价数量的vo
 * @CreateTime 2021年07月27日 11:34:00
 */
@Data
public class CommentLevelCountsVO {
    private Integer totalCounts;
    private Integer goodCounts;
    private Integer normalCounts;
    private Integer badCounts;
}
