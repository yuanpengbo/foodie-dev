package org.yuan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.yuan.service.OrderService;
import org.yuan.utils.DateUtil;

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder(){
        System.out.println(DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
        orderService.closeOrders();
    }
}
