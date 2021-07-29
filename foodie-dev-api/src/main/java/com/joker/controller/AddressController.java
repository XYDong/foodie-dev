package com.joker.controller;

import com.joker.pojo.UserAddress;
import com.joker.pojo.bo.UserAddressBO;
import com.joker.service.AddressService;
import com.joker.utils.JSONResult;
import com.joker.utils.MobileEmailUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@RestController
@Api(value = "地址相关",tags = {"地址相关接口api"})
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * 地址需求：
     * 1. 查询用户所有收货地址
     * 2.新增收货地址
     * 3. 删除收货地址
     * 4. 修改收货地址
     * 5. 设置默认地址
     */


    @ApiOperation(value = "根据用户id查询用户收货地址列表",notes = "根据用户id查询用户收货地址列表",httpMethod = "POST")
    @PostMapping("/list")
    public JSONResult list(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId){
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户id不能为空");
        }
        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return JSONResult.ok(userAddresses);
    }
    @ApiOperation(value = "新增用户收货地址",notes = "新增用户收货地址",httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestBody UserAddressBO addressBO){
        JSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressService.addNewAddress(addressBO);
        return JSONResult.ok();
    }

    private JSONResult checkAddress(UserAddressBO addressBO) {
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)) {
            return JSONResult.errorMsg("收货人不能为空");
        }
        if (receiver.length() > 12) {
            return JSONResult.errorMsg("收货人姓名不能太长");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)) {
            return JSONResult.errorMsg("收货人手机号不能为空");
        }
        if (mobile.length() != 11) {
            return JSONResult.errorMsg("收货人手机号长度不正确");
        }
        boolean isMobileOk = MobileEmailUtils.checkMobileIsOk(mobile);
        if (!isMobileOk) {
            return JSONResult.errorMsg("收货人手机号格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) ||
                StringUtils.isBlank(city) ||
                StringUtils.isBlank(district) ||
                StringUtils.isBlank(detail)) {
            return JSONResult.errorMsg("收货地址信息不能为空");
        }

        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户收货地址",notes = "修改用户收货地址",httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(
            @RequestBody UserAddressBO addressBO){
        if (StringUtils.isBlank(addressBO.getAddressId())) {
            return JSONResult.errorMsg("修改地址错误：addressId不能为空");
        }
        JSONResult checkRes = checkAddress(addressBO);
        if (checkRes.getStatus() != 200) {
            return checkRes;
        }
        addressService.updateAddress(addressBO);
        return JSONResult.ok();
    }
    @ApiOperation(value = "删除用户收货地址",notes = "删除用户收货地址",httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("删除地址错误：addressId或userId不能为空");
        }
        addressService.deleteAddress(userId,addressId);
        return JSONResult.ok();
    }
    @ApiOperation(value = "设置用户默认收货地址",notes = "设置用户默认收货地址",httpMethod = "POST")
    @PostMapping("/setDefault")
    public JSONResult setDefault(
            @ApiParam(name = "userId",value = "用户id",required = true)
            @RequestParam String userId,
            @ApiParam(name = "addressId",value = "地址id",required = true)
            @RequestParam String addressId){
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)) {
            return JSONResult.errorMsg("删除地址错误：addressId或userId不能为空");
        }
        addressService.updateAddress2Default(userId,addressId);
        return JSONResult.ok();
    }
}
