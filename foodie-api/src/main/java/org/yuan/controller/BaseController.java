package org.yuan.controller;


import java.io.File;

public class BaseController {

    public static final Integer COMMENT_PAGE_SIZE = 10;

    public static final Integer PAGE_SIZE = 20;

    public static final String FOODIE_SHOPCART = "shopcart";


    public static final String payReturnUrl = "http://localhost:8088/orders/notifyMerchantOrderPaid";
    public static final String paymentUrl = "http://localhost:8089/payment/createMerchantOrder";



    public static final String IMAGE_USER_FACE_LACATION = File.separator +"workspaces" +//"E:\\workspaces\\image\\foodie\\faces"
                                                            File.separator +"image" +
                                                            File.separator +"foodie" +
                                                            File.separator +"faces";


}
