package org.yuan.mapper;


import org.apache.ibatis.annotations.Param;
import org.yuan.pojo.OrderStatus;
import org.yuan.pojo.vo.MyOrdersVO;

import java.util.List;
import java.util.Map;

public interface OrdersMapperCustom{

    List<MyOrdersVO> queryMyOrders(@Param("paramMap") Map<String ,Object> map);

    Integer getMyOrderStatusCounts(@Param("paramMap") Map<String ,Object> map);

    List<OrderStatus> geMyOrderTrend(@Param("paramMap") Map<String ,Object> map);
}