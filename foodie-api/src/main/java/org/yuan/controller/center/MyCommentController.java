package org.yuan.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuan.enums.YesOrNo;
import org.yuan.pojo.OrderItems;
import org.yuan.pojo.Orders;
import org.yuan.pojo.bo.conter.MyCommentBO;
import org.yuan.service.center.MyCommentService;
import org.yuan.service.center.OrderService;
import org.yuan.utils.JSONResult;

import java.util.List;
import java.util.Optional;

@Api(value = "商品评价",tags = "商品评价相关接口")
@RestController
@RequestMapping("mycomments")
public class MyCommentController {

    @Autowired
    @Qualifier("ConterOrderServiceImpl")
    public OrderService orderService;

    @Autowired
    public MyCommentService myCommentService;

    @ApiOperation(value = "查询订单",notes = "查询订单",httpMethod = "POST")
    @PostMapping("pending")
    public JSONResult pending(
            @ApiParam(value = "userId",name = "用户Id",required = true)
            String userId,
            @ApiParam(value = "orderId",name = "商品Id",required = true)
            String orderId){

        JSONResult checkResult = checkUserOrder(orderId, userId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        Orders myOrder = (Orders)checkResult.getData();
        if(myOrder.getIsComment() == YesOrNo.YES.type){
            return JSONResult.errorMsg("该订单已经评价");
        }

        List<OrderItems> itemsList = myCommentService.queryPendingComment(orderId);
        return JSONResult.ok(itemsList);

    }

    private JSONResult checkUserOrder(String orderId,String userId){
        Orders orders = orderService.queryMyOrder(userId, orderId);
        if(!Optional.ofNullable(orders).isPresent()){
            return  JSONResult.errorMsg("订单不存在");
        }
        return JSONResult.ok(orders);
    }

    @ApiOperation(value = "保存评价列表",notes = "保存评价列表",httpMethod = "POST")
    @PostMapping("saveList")
    public JSONResult saveList(
            @ApiParam(value = "userId",name = "用户Id",required = true)
                    String userId,
            @ApiParam(value = "orderId",name = "商品Id",required = true)
                    String orderId,
            @RequestBody List<MyCommentBO> commentList){

        JSONResult checkResult = checkUserOrder(orderId, userId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        if(CollectionUtils.isEmpty(commentList)){
            return JSONResult.errorMsg("评论内容不能为空！");
        }

        myCommentService.saveCommnets(orderId,userId,commentList);

        return JSONResult.ok();
    }
}
