package com.cl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.cl.service.JiuzhentongzhiService;

@Configuration
@EnableScheduling
public class NotificationScheduleConfig {

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void retryFailedNotifications() {
        System.out.println("开始执行定时任务：重试失败的通知");
        jiuzhentongzhiService.retryFailedNotifications();
    }
}
