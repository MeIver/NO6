package com.cl.service;

import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.TongzhirenwuEntity;
import com.cl.entity.YishengyuyueEntity;

import java.util.Date;
import java.util.List;

/**
 * 通知服务接口
 * 处理就诊通知的发送、状态判断、失败记录和重试机制
 */
public interface NotificationService {

    /**
     * 预约审核通过后，立即创建所有后续通知任务
     * @param yuyue 预约实体
     */
    void createNotificationTasks(YishengyuyueEntity yuyue);

    /**
     * 发送通知
     * @param tongzhi 就诊通知实体
     * @return 是否发送成功
     */
    boolean sendNotification(JiuzhentongzhiEntity tongzhi);

    /**
     * 记录通知发送结果
     * @param tongzhi 就诊通知
     * @param success 是否成功
     * @param errorMsg 错误信息（失败时）
     */
    void recordNotificationResult(JiuzhentongzhiEntity tongzhi, boolean success, String errorMsg);

    /**
     * 获取需要重试的通知记录
     * @return 需要重试的通知记录列表
     */
    List<TongzhijiluEntity> getRetryNotifications();

    /**
     * 执行重试
     * @param jiluId 通知记录ID
     * @return 重试是否成功
     */
    boolean retryNotification(Long jiluId);

    /**
     * 获取待执行的通知任务
     * @return 待执行的任务列表
     */
    List<TongzhirenwuEntity> getPendingTasks();

    /**
     * 执行通知任务
     * @param taskId 任务ID
     */
    void executeTask(Long taskId);

    /**
     * 取消预约相关的所有通知任务
     * @param yuyueId 预约ID
     */
    void cancelNotificationsByYuyueId(Long yuyueId);

    /**
     * 计算下次重试时间（指数退避策略）
     * @param retryCount 已重试次数
     * @return 下次重试时间
     */
    Date calculateNextRetryTime(int retryCount);
}
