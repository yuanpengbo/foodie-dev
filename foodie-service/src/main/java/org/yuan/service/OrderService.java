package org.yuan.service;

import org.yuan.pojo.OrderStatus;
import org.yuan.pojo.Orders;
import org.yuan.pojo.UserAddress;
import org.yuan.pojo.bo.AddressBO;
import org.yuan.pojo.bo.SubmitOrderBO;
import org.yuan.pojo.vo.OrderVO;

import java.util.List;

public interface OrderService {

    OrderVO createOrder(SubmitOrderBO submitOrderBO);

    void updateOrderStatus(String orderId ,Integer orderStatus);

    OrderStatus queryOrderStatusById(String orderId);

}
