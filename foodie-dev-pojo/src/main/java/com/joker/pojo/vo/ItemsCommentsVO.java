package com.joker.pojo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @version 1.0.0
 * @ClassName ItemsCommentsVO.java
 * @Package com.joker.pojo.vo
 * @Author Joker
 * @Description 商品评价VO
 * @CreateTime 2021年07月27日 14:01:00
 */
@Data
public class ItemsCommentsVO {
    /**
     * 评价等级
     */
    private Integer commentLevel;
    /**
     * 评价内容
     */
    private String content;
    /**
     * 商品规格
     */
    private String specName;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 用户头像
     */
    private String userFace;
    /**
     * 用户昵称
     */
    private String nickname;
}
