package com.joker.service.impl;

import com.joker.enums.YesOrNo;
import com.joker.mapper.UserAddressMapper;
import com.joker.pojo.UserAddress;
import com.joker.pojo.bo.UserAddressBO;
import com.joker.service.AddressService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @version 1.0.0
 * @ClassName CarouselServiceImpl.java
 * @Package com.joker.service.impl
 * @Author Joker
 * @Description 轮播图接口实现
 * @CreateTime 2021年07月26日 15:56:00
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final UserAddressMapper addressMapper;
    private final Sid sid;

    public AddressServiceImpl(UserAddressMapper addressMapper, Sid sid) {
        this.addressMapper = addressMapper;
        this.sid = sid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        return addressMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewAddress(UserAddressBO userAddressBO) {
        // 1. 判断地址是否存在，如果没有，则新增为迷人地址
        Integer isDefault = 0;
        List<UserAddress> userAddresses = this.queryAll(userAddressBO.getUserId());
        if (userAddresses == null || userAddresses.isEmpty() || userAddresses.size() == 0) {
            isDefault = 1;
        }
        String addressId = sid.nextShort();
        // 2.保存到数据库
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setIsDefault(isDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        addressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAddress(UserAddressBO userAddressBO) {
        String addressId = userAddressBO.getAddressId();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        addressMapper.updateByPrimaryKeySelective(userAddress);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        addressMapper.delete(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateAddress2Default(String userId, String addressId) {
        // 1. 查找默认地址，设置为不默认
        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("isDefault",YesOrNo.YES.type);
        UserAddress userAddress = addressMapper.selectOneByExample(example);
        if (userAddress != null) {
            userAddress.setUserId(userId);
            userAddress.setIsDefault(YesOrNo.NO.type);
            userAddress.setUpdatedTime(new Date());
            addressMapper.updateByPrimaryKeySelective(userAddress);
        }

        // 2. 根据地址id，修改为默认的地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setId(addressId);
        defaultAddress.setUserId(userId);
        defaultAddress.setIsDefault(YesOrNo.YES.type);
        addressMapper.updateByPrimaryKeySelective(defaultAddress);
    }

    @Override
    public UserAddress queryUserAddres(String userId, String addressId) {
        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);

        return addressMapper.selectOne(singleAddress);
    }
}
