package org.yuan.controller;

public class BaseController {

    public static final Integer COMMENT_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";


    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    public static final String paymentUrl = "http://localhost:8089/payment/createMerchantOrder";
}
