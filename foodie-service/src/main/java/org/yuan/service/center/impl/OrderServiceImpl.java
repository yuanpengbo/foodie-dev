package org.yuan.service.center.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.enums.OrderStatusEnum;
import org.yuan.enums.YesOrNo;
import org.yuan.mapper.OrderStatusMapper;
import org.yuan.mapper.OrdersMapper;
import org.yuan.mapper.OrdersMapperCustom;
import org.yuan.pojo.OrderStatus;
import org.yuan.pojo.Orders;
import org.yuan.pojo.vo.MyOrdersVO;
import org.yuan.pojo.vo.OrderStatusCountsVO;
import org.yuan.service.center.OrderService;
import org.yuan.utils.PagedGridResult;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("ConterOrderServiceImpl")
public class OrderServiceImpl implements OrderService {

    @Autowired
    public OrdersMapperCustom ordersMapperCustom;

    @Autowired
    public OrderStatusMapper orderStatusMapper;

    @Autowired
    public OrdersMapper ordersMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryMyOrders(String userId, String orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId",userId);
        if(StringUtils.isNoneBlank(orderStatus)){
            param.put("orderStatus",orderStatus);
        }

        PageHelper.startPage(page,pageSize);
        List<MyOrdersVO> myOrdersList = ordersMapperCustom.queryMyOrders(param);

        return setterPagedGrid(myOrdersList,page);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateDeliverOrderStatus(String orderId) {
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.WAIT_RECEIVE.type);
        updateOrder.setDeliverTime(new Date());

        Example example =new Example(OrderStatus.class);
        example.createCriteria()
                .andEqualTo("orderId",orderId)
                .andEqualTo("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);
        orderStatusMapper.updateByExampleSelective(updateOrder,example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Orders queryMyOrder(String userId, String orderId) {
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setId(orderId);
        orders.setIsDelete(YesOrNo.NO.type);
        return ordersMapper.selectOne(orders);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateReceiveOrderStatus(String orderId) {
        OrderStatus updateOrder = new OrderStatus();
        updateOrder.setOrderStatus(OrderStatusEnum.SUCCESS.type);
        updateOrder.setSuccessTime(new Date());

        Example example = new Example(OrderStatus.class);
        example.createCriteria()
                .andEqualTo("orderId",orderId)
                .andEqualTo("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);

        return orderStatusMapper.updateByExampleSelective(updateOrder,example) > 0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteOrder(String userId, String orderId) {

        Orders orders = new Orders();
        orders.setIsDelete(YesOrNo.YES.type);
        orders.setUpdatedTime(new Date());

        Example example = new Example(Orders.class);
        example.createCriteria()
                .andEqualTo("id",orderId)
                .andEqualTo("userId",userId);
        return ordersMapper.updateByExampleSelective(orders,example) > 0;
    }


    private PagedGridResult setterPagedGrid(List<?> list, Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public OrderStatusCountsVO getMyOrderStatusCounts(String userId) {
        Map<String,Object> param = new HashMap<>();
        param.put("userId",userId);
        param.put("orderStatus",OrderStatusEnum.WAIT_PAY.type);
        int waitPauCounts = ordersMapperCustom.getMyOrderStatusCounts(param);

        param.put("orderStatus",OrderStatusEnum.WAIT_DELIVER.type);
        Integer waitDeliverCounts = ordersMapperCustom.getMyOrderStatusCounts(param);

        param.put("orderStatus",OrderStatusEnum.WAIT_RECEIVE.type);
        Integer waitReceiveCounts = ordersMapperCustom.getMyOrderStatusCounts(param);

        param.put("orderStatus",OrderStatusEnum.SUCCESS.type);
        param.put("isComment",YesOrNo.NO.type);
        Integer waitCommentCounts = ordersMapperCustom.getMyOrderStatusCounts(param);

        return new OrderStatusCountsVO(
                waitPauCounts,
                waitDeliverCounts,
                waitReceiveCounts,
                waitCommentCounts);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult geOrdersTrend(String userId, Integer page, Integer pageSize) {

        Map<String, Object> param = new HashMap<>();
        param.put("userId",userId);

        PageHelper.startPage(page,pageSize);
        List<OrderStatus> orderStatusList = ordersMapperCustom.geMyOrderTrend(param);
        return setterPagedGrid(orderStatusList,page);
    }
}
