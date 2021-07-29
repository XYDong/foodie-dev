package com.joker.service;

import com.joker.pojo.UserAddress;
import com.joker.pojo.bo.UserAddressBO;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName CarouselService.java
 * @Package com.joker.service
 * @Author Joker
 * @Description 轮播图
 * @CreateTime 2021年07月26日 15:51:00
 */
public interface AddressService {

    /**
     * 方法描述: <br>
     * <p> 根据用户id查询用户收货地址列表 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/29 9:08
     * @param userId 用户id
     * @return List<UserAddress>
     * @ReviseName
     * @ReviseTime 2021/7/29 9:08
     **/
    List<UserAddress> queryAll(String userId);
    /**
     * 方法描述: <br>
     * <p> 新增用户地址 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/29 9:24
     * @param userAddressBO 地址信息
     * @return void
     * @ReviseName
     * @ReviseTime 2021/7/29 9:24
     **/
    void addNewAddress(UserAddressBO userAddressBO);
    /**
     * 方法描述: <br>
     * <p> 修改用户地址 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/29 9:24
     * @param userAddressBO 地址信息
     * @return void
     * @ReviseName
     * @ReviseTime 2021/7/29 9:24
     **/
    void updateAddress(UserAddressBO userAddressBO);
    
    /**
     * 方法描述: <br>
     * <p> 根据地址id删除地址 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/29 9:50
     * @param userId 用户id
     * @param addressId 地址id
     * @ReviseName
     * @ReviseTime 2021/7/29 9:50
     **/
    void deleteAddress(String userId,String addressId);


    /**
     * 修改默认地址
     * @param userId
     * @param addressId
     */
    void updateAddress2Default(String userId,String addressId);

    /**
     * 根据用户id和地址id，查询具体的用户地址对象信息
     * @param userId
     * @param addressId
     * @return
     */
    UserAddress queryUserAddres(String userId, String addressId);

}
