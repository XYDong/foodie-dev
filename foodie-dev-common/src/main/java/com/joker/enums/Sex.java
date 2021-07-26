package com.joker.enums;

/**
 * @version 1.0.0
 * @ClassName Sex.java
 * @Package com.joker.enums
 * @Author Joker
 * @Description 性别枚举
 * @CreateTime 2021年07月26日 10:38:00
 */
public enum Sex {
    /**
     * 女
     */
    woman(0,"女"),
    /**
     * 男
     */
    man(1,"男"),
    /**
     * 保密
     */
    secret(2,"保密");

    public final Integer type;
    public final String value;

    Sex(Integer type, String value) {
        this.type = type;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
