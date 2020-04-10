package org.yuan.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuan.controller.BaseController;
import org.yuan.pojo.Orders;
import org.yuan.service.center.OrderService;
import org.yuan.utils.JSONResult;
import org.yuan.utils.PagedGridResult;

import java.util.Optional;

@Api(value = "个人的订单",tags = "个人订单相关接口")
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {
//
//    delete
//        confirmReceive

    @Autowired
    @Qualifier("ConterOrderServiceImpl")
    public OrderService orderService;

    @ApiOperation(value = "查询订单",notes = "查询订单",httpMethod = "POST")
    @PostMapping("query")
    public JSONResult query(
            @ApiParam(value = "userId",name = "用户Id",required = true)
            String userId,
            @ApiParam(value = "orderStatus",name = "订单状态")
            String orderStatus,Integer page,Integer pageSize
    ){

        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg(null);
        }

        if(!Optional.ofNullable(page).isPresent()){
            page = 1;
        }

        if(!Optional.ofNullable(pageSize).isPresent()){
            page = COMMENT_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = orderService.queryMyOrders(userId, orderStatus, page, pageSize);
        return JSONResult.ok(pagedGridResult);
    }


    @ApiOperation(value = "商家发货",notes = "商家发货",httpMethod = "GET")
    @GetMapping("deliver")
    public JSONResult deliver(
            @ApiParam(value = "orderId",name = "订单Id",required = true)
            String orderId){

        if(StringUtils.isBlank(orderId)){
            return JSONResult.errorMsg("订单ID不能为空");
        }

        orderService.updateDeliverOrderStatus(orderId);
        return JSONResult.ok();

    }

    @ApiOperation(value = "确认订单",notes = "确认订单",httpMethod = "POST")
    @PostMapping("confirmReceive")
    public JSONResult confirmReceive(
            @ApiParam(value = "orderId",name = "订单Id",required = true)
            String orderId,
            @ApiParam(value = "userId",name = "用户Id",required = true)
            String userId){
        JSONResult checkResult = checkUserOrder(orderId, userId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        boolean res = orderService.updateReceiveOrderStatus(orderId);
        if(!res){
            return JSONResult.errorMsg("确认失败");
        }
        return JSONResult.ok();
    }

    @ApiOperation(value = "删除订单",notes = "删除订单",httpMethod = "POST")
    @PostMapping("delete")
    public JSONResult delete(
            @ApiParam(value = "orderId",name = "订单Id",required = true)
                    String orderId,
            @ApiParam(value = "userId",name = "用户Id",required = true)
                    String userId){

        JSONResult checkResult = checkUserOrder(orderId, userId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        boolean res = orderService.deleteOrder(userId,orderId);
        if(!res){
            return JSONResult.errorMsg("确认失败");
        }
        return JSONResult.ok();
    }


    private JSONResult checkUserOrder(String orderId,String userId){
        Orders orders = orderService.queryMyOrder(userId, orderId);
        if(!Optional.ofNullable(orders).isPresent()){
            return  JSONResult.errorMsg("订单不存在");
        }
        return JSONResult.ok();
    }


}
