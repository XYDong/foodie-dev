package com.joker.enums;

/**
 * @version 1.0.0
 * @ClassName CommentLevel.java
 * @Package com.joker.enums
 * @Author Joker
 * @Description 商品评价等级
 * @CreateTime 2021年07月27日 11:36:00
 */
public enum CommentLevel {
    /**
     * 好评
     */
    GOOD(1,"好评"),
    /**
     * 中评
     */
    NORMAL(2,"中评"),

    /**
     * 差评
     */
    BAD(3,"差评");

    public final Integer type;
    public final String value;

    CommentLevel(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
