package org.yuan.service.center;

import org.yuan.pojo.Orders;
import org.yuan.utils.PagedGridResult;

public interface OrderService {

    PagedGridResult queryMyOrders(String userId, String orderStatus, Integer page, Integer pageSize);

    void updateDeliverOrderStatus(String orderId);

    Orders queryMyOrder(String userId,String orderId);

    boolean updateReceiveOrderStatus(String orderId);

    boolean deleteOrder(String userId,String orderId);
}
