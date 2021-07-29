package com.joker.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@ApiModel(value = "新增用户收货地址BO", description = "从客户端传入")
public class UserAddressBO {

    /**
     * 地址id（用于判断是新增还是修改）
     */
    @ApiModelProperty(value = "地址id",name = "addressId",required = true)
    private String addressId;
    /**
     * 关联用户id
     */
    @ApiModelProperty(value = "用户id",name = "userId",required = true)
    private String userId;

    /**
     * 收件人姓名
     */
    @ApiModelProperty(value = "收件人姓名",name = "receiver",required = true)
    private String receiver;

    /**
     * 收件人手机号
     */
    @ApiModelProperty(value = "收件人手机号",name = "mobile",required = true)
    private String mobile;

    /**
     * 省份
     */
    @ApiModelProperty(value = "省份",name = "province",required = true)
    private String province;

    /**
     * 城市
     */
    @ApiModelProperty(value = "城市",name = "city",required = true)
    private String city;

    /**
     * 区县
     */
    @ApiModelProperty(value = "区县",name = "district",required = true)
    private String district;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址",name = "detail",required = true)
    private String detail;

    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段",name = "extand")
    private String extand;



}