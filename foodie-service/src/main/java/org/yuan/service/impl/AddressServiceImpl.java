package org.yuan.service.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.yuan.enums.YesOrNo;
import org.yuan.mapper.UserAddressMapper;
import org.yuan.pojo.UserAddress;
import org.yuan.pojo.bo.AddressBO;
import org.yuan.service.AddressService;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {
        Example example = new Example(UserAddress.class);
        example.createCriteria()
                .andEqualTo("userId",userId);
        return userAddressMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        List<UserAddress> userAddresses = this.queryAll(addressBO.getUserId());
        if(CollectionUtils.isEmpty(userAddresses)){
            userAddress.setIsDefault(YesOrNo.YES.type);
        }
        userAddress.setId(sid.nextShort());
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setUpdatedTime(new Date());
        userAddress.setId(addressBO.getAddressId());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddressMapper.delete(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddressToBeDefault(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.YES.type);
        List<UserAddress> userAddressList = userAddressMapper.select(userAddress);
        userAddressList.stream().forEach(address -> {
            address.setIsDefault(YesOrNo.NO.type);
            userAddressMapper.updateByPrimaryKeySelective(address);
        });

        UserAddress defaultUserAddress = new UserAddress();
        defaultUserAddress.setId(addressId);
        defaultUserAddress.setUserId(userId);
        defaultUserAddress.setIsDefault(YesOrNo.YES.type);
        userAddressMapper.updateByPrimaryKeySelective(defaultUserAddress);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public UserAddress queryUserAddress(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);
        return userAddressMapper.selectOne(userAddress);
    }
}
