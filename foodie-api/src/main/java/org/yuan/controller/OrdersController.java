package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yuan.pojo.bo.ShopCartBO;
import org.yuan.pojo.bo.SubmitOrderBO;
import org.yuan.service.OrderService;
import org.yuan.utils.CookieUtils;
import org.yuan.utils.JSONResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "订单模块" ,tags ={"订单模块相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    OrderService orderService;

    @ApiOperation(value = "用户下单" ,notes = "用户下单" ,httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response){
        try{
            String orderId = orderService.createOrder(submitOrderBO);
            CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);


            return JSONResult.ok(orderId);
        }catch (Exception e){
            return JSONResult.errorMsg(e.getMessage());
        }


    }


}
