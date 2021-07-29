package com.joker.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @ClassName SubmitOrderBO.java
 * @Package com.joker.pojo.bo
 * @Author Joker
 * @Description 用于创建订单BO的对象
 * @CreateTime 2021年07月29日 11:04:00
 */
@Data
@ApiModel(value = "用于创建订单BO的对象", description = "从客户端传入")
public class SubmitOrderBO {

    @ApiModelProperty(value = "用户id",name = "userId",required = true)
    private String userId;
    @ApiModelProperty(value = "商品规格字符串",name = "itemSpecIds",example = "1001,1002",required = true)
    private String itemSpecIds;
    @ApiModelProperty(value = "收货地址id",name = "addressId",required = true)
    private String addressId;
    @ApiModelProperty(value = "支付方式",name = "payMethod",required = true)
    private Integer payMethod;
    @ApiModelProperty(value = "备注",name = "leftMsg",required = true)
    private String leftMsg;
}
