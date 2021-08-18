package com.joker.controller;

import com.joker.enums.OrderStatusEnum;
import com.joker.enums.PayMethod;
import com.joker.pojo.OrderStatus;
import com.joker.pojo.bo.ShopcartBO;
import com.joker.pojo.bo.SubmitOrderBO;
import com.joker.pojo.vo.MerchantOrdersVO;
import com.joker.pojo.vo.OrderVO;
import com.joker.service.OrderService;
import com.joker.utils.CookieUtils;
import com.joker.utils.JSONResult;
import com.joker.utils.JsonUtils;
import com.joker.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @version 1.0.0
 * @ClassName OrdersController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description 订单相关接口
 * @CreateTime 2021年07月29日 11:01:00
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    private final OrderService orderService;

    private final RestTemplate restTemplate;

    private final RedisOperator redisOperator;


    @Autowired
    public OrdersController(OrderService orderService, RestTemplate restTemplate, RedisOperator operator) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
        this.redisOperator = operator;
    }

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
            return JSONResult.errorMsg("支付方式不支持！");
        }

//        System.out.println(submitOrderBO.toString());

        // 1. 创建订单
        String shopcartStr = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopcartStr)) {
            return JSONResult.errorMsg("购物车数据不正确");
        }
        List<ShopcartBO> shopcartBOS = JsonUtils.jsonToList(shopcartStr, ShopcartBO.class);

        OrderVO orderVO = orderService.createOrder(shopcartBOS,submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        assert shopcartBOS != null;
        shopcartBOS.removeAll(orderVO.getToBeRemovedShopcartList());
        redisOperator.set(FOODIE_SHOPCART+":"+submitOrderBO.getUserId(),JsonUtils.objectToJson(shopcartBOS));
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, "", true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(payReturnUrl);

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");

        HttpEntity<MerchantOrdersVO> entity =
                new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JSONResult> responseEntity =
                restTemplate.postForEntity(paymentUrl,
                        entity,
                        JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            logger.error("发送错误：{}", paymentResult.getMsg());
            return JSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }

        return JSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(orderStatus);
    }
}
