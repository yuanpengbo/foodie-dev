package org.yuan.service;

import org.yuan.pojo.UserAddress;
import org.yuan.pojo.bo.AddressBO;
import org.yuan.pojo.bo.SubmitOrderBO;

import java.util.List;

public interface OrderService {

    String createOrder(SubmitOrderBO submitOrderBO);


}
