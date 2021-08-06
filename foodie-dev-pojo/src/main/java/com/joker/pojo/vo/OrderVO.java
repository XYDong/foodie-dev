package com.joker.pojo.vo;

import com.joker.pojo.bo.ShopcartBO;
import lombok.Data;

import java.util.List;

@Data
public class OrderVO {

    private String orderId;
    private MerchantOrdersVO merchantOrdersVO;

    List<ShopcartBO> toBeRemovedShopcartList;

}