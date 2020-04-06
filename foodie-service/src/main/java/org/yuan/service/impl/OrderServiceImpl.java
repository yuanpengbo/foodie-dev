package org.yuan.service.impl;


import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.enums.OrderStatusEnum;
import org.yuan.enums.YesOrNo;
import org.yuan.mapper.OrderItemsMapper;
import org.yuan.mapper.OrderStatusMapper;
import org.yuan.mapper.OrdersMapper;
import org.yuan.pojo.*;
import org.yuan.pojo.bo.SubmitOrderBO;
import org.yuan.pojo.vo.MerchantOrdersVO;
import org.yuan.pojo.vo.OrderVO;
import org.yuan.service.AddressService;
import org.yuan.service.ItemService;
import org.yuan.service.OrderService;
import org.yuan.utils.DateUtil;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public OrderVO createOrder(SubmitOrderBO submitOrderBO) {
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



        orders.setPostAmount(postAmount);

        orders.setPayMethod(payMethod);
        orders.setLeftMsg(leftMsg);

        orders.setIsComment(YesOrNo.NO.type);
        orders.setIsDelete(YesOrNo.NO.type);

        orders.setCreatedTime(new Date());
        orders.setUpdatedTime(new Date());

        List<String> specIdsList = Arrays.asList(itemSpecIds.split(","));
        AtomicReference<Integer> totalAmount = new AtomicReference<>(0);
        AtomicReference<Integer> realPyAmount = new AtomicReference<>(0);
        specIdsList.stream().forEach(spec->{
            //TODO 整合redis后 商品购买数量重新送redis的购物车中获取
            Integer buyCounts = 1;
            ItemsSpec itemsSpec = itemService.queryItemSpecById(spec);
            totalAmount.set( totalAmount.get()+ itemsSpec.getPriceNormal() * buyCounts);
            realPyAmount.getAndSet(realPyAmount.get() + itemsSpec.getPriceDiscount() * buyCounts);

            //根据规格ID 获取商品信息和商品图片
            Items item = itemService.queryItemByID(itemsSpec.getItemId());

            String url = itemService.queryItemMainImgUrlById(itemsSpec.getItemId());

            //保持子订单到数据库
            OrderItems subOderItem = new OrderItems();
            subOderItem.setId(sid.nextShort());
            subOderItem.setOrderId(orders.getId());
            subOderItem.setItemId(item.getId());
            subOderItem.setItemName(item.getItemName());
            subOderItem.setItemImg(url);
            subOderItem.setBuyCounts(buyCounts);
            subOderItem.setItemSpecId(spec);
            subOderItem.setItemSpecName(itemsSpec.getName());
            subOderItem.setPrice(itemsSpec.getPriceDiscount());
            orderItemsMapper.insert(subOderItem);

            //更新库存
            itemService.decreaseItemSpecStock(spec,buyCounts);
        });

        orders.setTotalAmount(totalAmount.get());
        orders.setRealPayAmount(realPyAmount.get());

        ordersMapper.insert(orders);

        //订单状态
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orders.getId());
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());

        orderStatusMapper.insert(waitPayOrderStatus);

        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orders.getId());
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPyAmount.get() + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);
//        merchantOrdersVO.setReturnUrl();

        return new OrderVO(orders.getId(),merchantOrdersVO);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus paidStatus = new OrderStatus();
        paidStatus.setOrderId(orderId);
        paidStatus.setOrderStatus(orderStatus);
        paidStatus.setPayTime(new Date());
        orderStatusMapper.updateByPrimaryKey(paidStatus);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatus queryOrderStatusById(String orderId) {
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void closeOrders() {
        OrderStatus queryOrders = new OrderStatus();
        queryOrders.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> list = orderStatusMapper.select(queryOrders);
        list.stream().forEach(orderStatus->{
            Date createdTime = orderStatus.getCreatedTime();
            int days = DateUtil.daysBetween(createdTime, new Date());
            if(days >= 1){
                doCloseOrder(orderStatus.getOrderId());
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void doCloseOrder(String OrderId){
        OrderStatus closeOrder = new OrderStatus();
        closeOrder.setOrderId(OrderId);
        closeOrder.setOrderStatus(OrderStatusEnum.CLOSE.type);
        closeOrder.setCloseTime(new Date());
        orderStatusMapper.updateByPrimaryKeySelective(closeOrder);
    }
}
