package org.yuan.service.center.impl;

import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.enums.YesOrNo;
import org.yuan.mapper.*;
import org.yuan.pojo.OrderItems;
import org.yuan.pojo.OrderStatus;
import org.yuan.pojo.Orders;
import org.yuan.pojo.bo.conter.MyCommentBO;
import org.yuan.service.center.MyCommentService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MyCommentServiceImpl implements MyCommentService {


    @Autowired
    public OrderItemsMapper orderItemsMapper;

    @Autowired
    public ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    @Autowired
    ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    OrderStatusMapper orderStatusMapper;

    @Autowired
    public Sid sid;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<OrderItems> queryPendingComment(String orderId) {
        OrderItems query = new OrderItems();
        query.setOrderId(orderId);
        return  orderItemsMapper.select(query);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveCommnets(String orderId, String userId, List<MyCommentBO> orderItemList) {

        orderItemList.stream().forEach(order->{
            order.setCommentId(sid.nextShort());
        });
        Map<String,Object> map = new HashMap<>();
        map.put("userId",userId);
        map.put("commentList",orderItemList);
        itemsCommentsMapperCustom.saveComments(map);

        Orders orders = new Orders();
        orders.setId(orderId);
        orders.setIsComment(YesOrNo.YES.type);
        ordersMapper.updateByPrimaryKeySelective(orders);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setCommentTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }

}
