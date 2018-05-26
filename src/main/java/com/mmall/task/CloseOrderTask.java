package com.mmall.task;

import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: wangcheng
 * @Date: Created in 9:42 2018/5/26
 */

@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService iOrderService;

    @Scheduled(cron = "0 */1 * * * ?") //每一分钟的整数倍
    public void closeOrderTask1(){
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.tast.time.hour"));
        iOrderService.closeOrder(hour);
        log.info("关闭订单定时任务结束");
    }
}
