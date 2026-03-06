package com.cl.config;

import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.TongzhirenwuEntity;
import com.cl.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * 定时任务配置
 * 用于定时执行通知任务和重试失败的通知
 */
@Configuration
@EnableScheduling
public class ScheduledConfig {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledConfig.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * 每分钟执行一次，处理待执行的通知任务
     */
    @Scheduled(fixedRate = 60000)
    public void executePendingTasks() {
        try {
            List<TongzhirenwuEntity> pendingTasks = notificationService.getPendingTasks();
            if (pendingTasks != null && !pendingTasks.isEmpty()) {
                logger.info("发现 {} 个待执行的通知任务", pendingTasks.size());
                for (TongzhirenwuEntity task : pendingTasks) {
                    try {
                        notificationService.executeTask(task.getId());
                        logger.info("执行任务成功，任务ID: {}", task.getId());
                    } catch (Exception e) {
                        logger.error("执行任务失败，任务ID: {}", task.getId(), e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("执行待处理任务时发生错误", e);
        }
    }

    /**
     * 每5分钟执行一次，重试失败的通知
     */
    @Scheduled(fixedRate = 300000)
    public void retryFailedNotifications() {
        try {
            List<TongzhijiluEntity> retryList = notificationService.getRetryNotifications();
            if (retryList != null && !retryList.isEmpty()) {
                logger.info("发现 {} 个需要重试的通知", retryList.size());
                for (TongzhijiluEntity jilu : retryList) {
                    try {
                        boolean success = notificationService.retryNotification(jilu.getId());
                        logger.info("重试通知结果: {}, 记录ID: {}", success ? "成功" : "失败", jilu.getId());
                    } catch (Exception e) {
                        logger.error("重试通知失败，记录ID: {}", jilu.getId(), e);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("重试失败通知时发生错误", e);
        }
    }
}
