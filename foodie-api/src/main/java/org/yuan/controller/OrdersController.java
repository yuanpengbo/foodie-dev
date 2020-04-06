package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.yuan.enums.OrderStatusEnum;
import org.yuan.pojo.OrderStatus;
import org.yuan.pojo.bo.ShopCartBO;
import org.yuan.pojo.bo.SubmitOrderBO;
import org.yuan.pojo.vo.MerchantOrdersVO;
import org.yuan.pojo.vo.OrderVO;
import org.yuan.service.OrderService;
import org.yuan.utils.CookieUtils;
import org.yuan.utils.JSONResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Api(value = "订单模块" ,tags ={"订单模块相关接口"})
@RestController
@RequestMapping("orders")
public class OrdersController extends BaseController {

    @Autowired
    OrderService orderService;

    @Autowired
    RestTemplate restTemplate;

    @ApiOperation(value = "用户下单" ,notes = "用户下单" ,httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response){
        try{
            OrderVO order = orderService.createOrder(submitOrderBO);
            CookieUtils.setCookie(request,response,FOODIE_SHOPCART,"",true);


            order.getMerchantOrdersVO().setReturnUrl(payReturnUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            httpHeaders.add("payUeserId","yuan");
            httpHeaders.add("password","yuan123");
            HttpEntity<MerchantOrdersVO> entity = new HttpEntity<>(order.getMerchantOrdersVO(),httpHeaders);

            ResponseEntity<JSONResult> responseEntity = restTemplate.postForEntity(paymentUrl, entity, JSONResult.class);

            JSONResult paymentResult = responseEntity.getBody();
            if(!paymentResult.isOK()){
                return JSONResult.errorMsg("支付失败");
            }

            return JSONResult.ok(order.getOrderId());
        }catch (Exception e){
            e.printStackTrace();
            return JSONResult.errorMsg(e.getMessage());
        }
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.queryOrderStatusById(orderId);
        return JSONResult.ok(orderStatus);
    }


}
