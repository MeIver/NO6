package com.cl.controller;

import com.cl.service.NotificationService;
import com.cl.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 通知管理控制器
 * 提供通知重试等管理接口
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 重试发送失败的通知
     * @param jiluId 通知记录ID
     * @return 操作结果
     */
    @PostMapping("/retry/{jiluId}")
    public R retryNotification(@PathVariable("jiluId") Long jiluId) {
        try {
            boolean success = notificationService.retryNotification(jiluId);
            if (success) {
                return R.ok("重试成功");
            } else {
                return R.error("重试失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("重试异常：" + e.getMessage());
        }
    }

    /**
     * 手动触发执行待处理任务
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/execute/{taskId}")
    public R executeTask(@PathVariable("taskId") Long taskId) {
        try {
            notificationService.executeTask(taskId);
            return R.ok("任务执行成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("任务执行异常：" + e.getMessage());
        }
    }

    /**
     * 取消预约相关的所有通知任务
     * @param yuyueId 预约ID
     * @return 操作结果
     */
    @PostMapping("/cancel/{yuyueId}")
    public R cancelNotifications(@PathVariable("yuyueId") Long yuyueId) {
        try {
            notificationService.cancelNotificationsByYuyueId(yuyueId);
            return R.ok("通知任务已取消");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("取消失败：" + e.getMessage());
        }
    }
}
