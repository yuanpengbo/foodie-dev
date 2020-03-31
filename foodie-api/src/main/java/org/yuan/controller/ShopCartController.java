package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.yuan.pojo.bo.ShopCartBO;
import org.yuan.utils.CookieUtils;
import org.yuan.utils.JSONResult;
import org.yuan.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "购物车接口" ,tags ={"购物车相关接口"})
@RestController
@RequestMapping("shopcart")
public class ShopCartController {

    @ApiOperation(value = "添加购物车" ,notes = "添加购物车" ,httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(
            @RequestParam  String userId,
            @RequestBody ShopCartBO shopCartBO,
            HttpServletRequest request,
            HttpServletResponse response){

        if(StringUtils.isBlank(userId)){
            return JSONResult.errorMsg("");
        }

        System.out.println(shopCartBO);

        //TODO 前端用户在登录情况下 ，添加商品到购物车，会同时在后端同步购物车到redis缓存
        return JSONResult.ok();
    }


    @ApiOperation(value = "从购物车删除商品" ,notes = "从购物车删除商品" ,httpMethod = "POST")
    @PostMapping("/del")
    public JSONResult del(
            @RequestParam  String userId,
            @RequestParam  String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response){

        if(StringUtils.isBlank(userId) && StringUtils.isBlank(itemSpecId)){
            return JSONResult.errorMsg("");
        }


        //TODO 删除购物车中的商品时  同步redis
        return JSONResult.ok();
    }



}
