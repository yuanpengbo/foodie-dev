package org.yuan.service;

import org.yuan.pojo.UserAddress;
import org.yuan.pojo.bo.AddressBO;

import java.util.List;

public interface AddressService {

    List<UserAddress> queryAll(String userId);

    void addNewUserAddress(AddressBO addressBO);

    void updateUserAddress(AddressBO addressBO);

    void deleteUserAddress(String userId,String addressId);

    void updateUserAddressToBeDefault(String userId,String addressId);

    UserAddress queryUserAddress(String userId,String addressId);
}
