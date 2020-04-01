package org.yuan.service.impl;


import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.enums.YesOrNo;
import org.yuan.mapper.OrdersMapper;
import org.yuan.mapper.UserAddressMapper;
import org.yuan.pojo.Orders;
import org.yuan.pojo.UserAddress;
import org.yuan.pojo.bo.SubmitOrderBO;
import org.yuan.service.AddressService;
import org.yuan.service.DefaultService;
import org.yuan.service.OrderService;

import java.util.Date;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void createOrder(SubmitOrderBO submitOrderBO) {
        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        Integer payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();

        Integer postAmount = 0;

        UserAddress userAddress = addressService.queryUserAddress(userId, addressId);

        Orders orders = new Orders();
        orders.setId(sid.nextShort());
        orders.setUserId(userId);
        orders.setReceiverName(userAddress.getReceiver());
        orders.setReceiverMobile(userAddress.getMobile());

        StringBuilder receiverAddress = new StringBuilder();
        receiverAddress.append(userAddress.getProvince())
                .append(" ")
                .append(userAddress.getCity())
                .append(" ")
                .append(userAddress.getDistrict())
                .append(" ")
                .append(userAddress.getDetail());

        orders.setReceiverAddress(receiverAddress.toString());


//        orders.setTotalAmount();
//        orders.setRealPayAmount();
        orders.setPostAmount(postAmount);

        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);

        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);

        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());


    }
}
