package com.joker.enums;

/**
 * @version 1.0.0
 * @ClassName YesOrNo.java
 * @Package com.joker.enums
 * @Author Joker
 * @Description 是否的枚举
 * @CreateTime 2021年07月26日 16:02:00
 */
public enum YesOrNo {
    /**
     * 是
     */
    NO(0,"否"),
    /**
     * 否
     */
    YES(1,"是");

    public final Integer type;
    public final String value;

    YesOrNo(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
