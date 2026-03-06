package com.cl.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.TongzhijiluEntity;
import com.cl.entity.TongzhirenwuEntity;
import com.cl.entity.YishengyuyueEntity;
import com.cl.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 通知服务实现类
 * 处理就诊通知的发送、状态判断、失败记录和重试机制
 */
@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private JiuzhentongzhiService jiuzhentongzhiService;

    @Autowired
    private TongzhijiluService tongzhijiluService;

    @Autowired
    private TongzhirenwuService tongzhirenwuService;

    // 最大重试次数
    private static final int MAX_RETRY_COUNT = 3;
    // 重试间隔基数（分钟）
    private static final int RETRY_BASE_INTERVAL = 5;

    /**
     * 预约审核通过后，立即创建所有后续通知任务
     * 预约成功后立刻发送所有后续提醒
     */
    @Override
    @Transactional
    public void createNotificationTasks(YishengyuyueEntity yuyue) {
        if (yuyue == null || yuyue.getYuyueshijian() == null) {
            logger.warn("预约信息为空或预约时间为空，无法创建通知任务");
            return;
        }

        logger.info("开始为用户 [{}] 创建预约通知任务，预约时间: {}", yuyue.getZhanghao(), yuyue.getYuyueshijian());

        Date yuyueTime = yuyue.getYuyueshijian();
        Date now = new Date();

        // 1. 立即发送预约成功通知
        logger.info("发送预约成功通知给用户 [{}]", yuyue.getZhanghao());
        createAndSendImmediateNotification(yuyue, "预约成功", now);

        // 2. 创建就诊前24小时提醒任务（如果预约时间大于24小时）
        Date before24Hour = new Date(yuyueTime.getTime() - 24 * 60 * 60 * 1000);
        if (before24Hour.after(now)) {
            logger.info("创建就诊前24小时提醒任务，计划发送时间: {}", before24Hour);
            createNotificationTask(yuyue, "就诊前24小时提醒", before24Hour, 3);
        }

        // 3. 创建就诊前1小时提醒任务（如果预约时间大于1小时）
        Date before1Hour = new Date(yuyueTime.getTime() - 60 * 60 * 1000);
        if (before1Hour.after(now)) {
            logger.info("创建就诊前1小时提醒任务，计划发送时间: {}", before1Hour);
            createNotificationTask(yuyue, "就诊前1小时提醒", before1Hour, 2);
        }

        // 4. 创建就诊时间提醒任务
        logger.info("创建就诊时间提醒任务，计划发送时间: {}", yuyueTime);
        createNotificationTask(yuyue, "就诊时间提醒", yuyueTime, 1);

        logger.info("用户 [{}] 的预约通知任务创建完成", yuyue.getZhanghao());
    }

    /**
     * 创建并立即发送通知
     */
    private void createAndSendImmediateNotification(YishengyuyueEntity yuyue, String tongzhiLeixing, Date tongzhiTime) {
        // 创建就诊通知
        JiuzhentongzhiEntity tongzhi = new JiuzhentongzhiEntity();
        tongzhi.setTongzhibianhao(generateTongzhiBianhao());
        tongzhi.setYishengzhanghao(yuyue.getYishengzhanghao());
        tongzhi.setDianhua(yuyue.getDianhua());
        tongzhi.setJiuzhenshijian(yuyue.getYuyueshijian());
        tongzhi.setTongzhishijian(tongzhiTime);
        tongzhi.setZhanghao(yuyue.getZhanghao());
        tongzhi.setShouji(yuyue.getShouji());
        tongzhi.setTongzhibeizhu(buildTongzhiContent(tongzhiLeixing, yuyue));
        tongzhi.setYuyueId(yuyue.getId());
        tongzhi.setTongzhiLeixing(tongzhiLeixing);
        tongzhi.setTongzhiZhuangtai("待发送");
        tongzhi.setFasonCishu(0);

        jiuzhentongzhiService.insert(tongzhi);

        // 立即发送
        boolean success = sendNotification(tongzhi);

        // 更新通知状态
        tongzhi.setTongzhiZhuangtai(success ? "已发送" : "发送失败");
        tongzhi.setFasonCishu(1);
        tongzhi.setZuizhongFasonShijian(new Date());
        jiuzhentongzhiService.updateById(tongzhi);

        // 记录发送结果
        recordNotificationResult(tongzhi, success, success ? null : "首次发送失败");
    }

    /**
     * 创建通知任务（定时发送）
     */
    private void createNotificationTask(YishengyuyueEntity yuyue, String tongzhiLeixing, Date jihuaFasonShijian, int youxianji) {
        TongzhirenwuEntity task = new TongzhirenwuEntity();
        task.setYuyueId(yuyue.getId());
        task.setZhanghao(yuyue.getZhanghao());
        task.setShouji(yuyue.getShouji());
        task.setYishengzhanghao(yuyue.getYishengzhanghao());
        task.setDianhua(yuyue.getDianhua());
        task.setYuyueshijian(yuyue.getYuyueshijian());
        task.setTongzhiLeixing(tongzhiLeixing);
        task.setJihuaFasonShijian(jihuaFasonShijian);
        task.setZhuangtai("待执行");
        task.setYouxianji(youxianji);

        tongzhirenwuService.insert(task);
    }

    /**
     * 构建通知内容
     */
    private String buildTongzhiContent(String tongzhiLeixing, YishengyuyueEntity yuyue) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder content = new StringBuilder();

        switch (tongzhiLeixing) {
            case "预约成功":
                content.append("您的预约已成功！预约时间：").append(sdf.format(yuyue.getYuyueshijian()))
                       .append("，医生账号：").append(yuyue.getYishengzhanghao());
                break;
            case "就诊前24小时提醒":
                content.append("温馨提醒：您预约的就诊将在24小时后（").append(sdf.format(yuyue.getYuyueshijian()))
                       .append("）开始，请做好准备。");
                break;
            case "就诊前1小时提醒":
                content.append("温馨提醒：您预约的就诊将在1小时后（").append(sdf.format(yuyue.getYuyueshijian()))
                       .append("）开始，请准时到达。");
                break;
            case "就诊时间提醒":
                content.append("就诊提醒：您的就诊时间已到（").append(sdf.format(yuyue.getYuyueshijian()))
                       .append("），请前往就诊。");
                break;
            default:
                content.append("就诊通知：").append(tongzhiLeixing);
        }

        return content.toString();
    }

    /**
     * 生成通知编号
     */
    private String generateTongzhiBianhao() {
        return "TZ" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4);
    }

    /**
     * 发送通知
     * 这里模拟发送逻辑，实际项目中可以接入短信API、推送服务等
     */
    @Override
    public boolean sendNotification(JiuzhentongzhiEntity tongzhi) {
        try {
            logger.info("正在发送 [{}] 通知给用户 [{}]，手机号: {}", 
                tongzhi.getTongzhiLeixing(), tongzhi.getZhanghao(), tongzhi.getShouji());
            
            // 模拟发送延迟
            Thread.sleep(100);

            // 实际项目中，这里应该调用短信API或推送服务
            // 例如：SMSUtil.sendSMS(tongzhi.getShouji(), tongzhi.getTongzhibeizhu());

            // 模拟随机失败（用于测试重试机制）
            // return Math.random() > 0.3;

            logger.info("[{}] 通知发送成功！用户: [{}]", tongzhi.getTongzhiLeixing(), tongzhi.getZhanghao());
            return true;
        } catch (Exception e) {
            logger.error("[{}] 通知发送失败！用户: [{}]，错误: {}", 
                tongzhi.getTongzhiLeixing(), tongzhi.getZhanghao(), e.getMessage());
            return false;
        }
    }

    /**
     * 记录通知发送结果
     */
    @Override
    @Transactional
    public void recordNotificationResult(JiuzhentongzhiEntity tongzhi, boolean success, String errorMsg) {
        logger.info("记录通知发送结果 - 用户: [{}], 类型: [{}], 状态: {}", 
            tongzhi.getZhanghao(), tongzhi.getTongzhiLeixing(), success ? "成功" : "失败");
        
        TongzhijiluEntity jilu = new TongzhijiluEntity();
        jilu.setJiuzhentongzhiId(tongzhi.getId());
        jilu.setYuyueId(tongzhi.getYuyueId());
        jilu.setZhanghao(tongzhi.getZhanghao());
        jilu.setShouji(tongzhi.getShouji());
        jilu.setTongzhiLeixing(tongzhi.getTongzhiLeixing());
        jilu.setFasonShijian(new Date());
        jilu.setFasonZhuangtai(success ? "成功" : "失败");
        jilu.setShibaiYuanyin(errorMsg);
        jilu.setChongshiCishu(tongzhi.getFasonCishu() != null ? tongzhi.getFasonCishu() : 0);

        if (!success && jilu.getChongshiCishu() < MAX_RETRY_COUNT) {
            jilu.setXiaciChongshiShijian(calculateNextRetryTime(jilu.getChongshiCishu()));
            jilu.setChuliZhuangtai("未处理");
            logger.warn("通知发送失败，将在 {} 后重试，当前重试次数: {}", 
                jilu.getXiaciChongshiShijian(), jilu.getChongshiCishu());
        } else if (!success) {
            jilu.setChuliZhuangtai("未处理");
            logger.error("通知发送失败，已达到最大重试次数 [{}]，需要人工处理", MAX_RETRY_COUNT);
        } else {
            jilu.setChuliZhuangtai("已处理");
        }

        tongzhijiluService.insert(jilu);
        logger.info("通知记录已保存，记录ID: {}", jilu.getId());
    }

    /**
     * 获取需要重试的通知记录
     * 条件：发送失败、重试次数小于最大次数、到达下次重试时间
     */
    @Override
    public List<TongzhijiluEntity> getRetryNotifications() {
        EntityWrapper<TongzhijiluEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("fason_zhuangtai", "失败")
               .lt("chongshi_cishu", MAX_RETRY_COUNT)
               .le("xiaci_chongshi_shijian", new Date())
               .eq("chuli_zhuangtai", "未处理")
               .orderBy("addtime", true);

        return tongzhijiluService.selectList(wrapper);
    }

    /**
     * 执行重试
     */
    @Override
    @Transactional
    public boolean retryNotification(Long jiluId) {
        TongzhijiluEntity jilu = tongzhijiluService.selectById(jiluId);
        if (jilu == null) {
            return false;
        }

        JiuzhentongzhiEntity tongzhi = jiuzhentongzhiService.selectById(jilu.getJiuzhentongzhiId());
        if (tongzhi == null) {
            return false;
        }

        // 更新重试次数
        int newRetryCount = jilu.getChongshiCishu() + 1;
        tongzhi.setFasonCishu(newRetryCount);

        // 执行发送
        boolean success = sendNotification(tongzhi);

        // 更新通知状态
        tongzhi.setTongzhiZhuangtai(success ? "已发送" : "发送失败");
        tongzhi.setZuizhongFasonShijian(new Date());
        jiuzhentongzhiService.updateById(tongzhi);

        // 更新记录
        jilu.setChongshiCishu(newRetryCount);
        jilu.setFasonShijian(new Date());
        jilu.setFasonZhuangtai(success ? "成功" : "失败");

        if (!success && newRetryCount < MAX_RETRY_COUNT) {
            jilu.setXiaciChongshiShijian(calculateNextRetryTime(newRetryCount));
        } else if (success) {
            jilu.setChuliZhuangtai("已处理");
            jilu.setChuliShijian(new Date());
        }

        tongzhijiluService.updateById(jilu);

        // 如果还是失败，创建新的记录
        if (!success && newRetryCount < MAX_RETRY_COUNT) {
            recordNotificationResult(tongzhi, false, "第" + newRetryCount + "次重试失败");
        }

        return success;
    }

    /**
     * 获取待执行的通知任务
     */
    @Override
    public List<TongzhirenwuEntity> getPendingTasks() {
        EntityWrapper<TongzhirenwuEntity> wrapper = new EntityWrapper<>();
        wrapper.eq("zhuangtai", "待执行")
               .le("jihua_fason_shijian", new Date())
               .orderBy("youxianji", true)
               .orderBy("jihua_fason_shijian", true);

        return tongzhirenwuService.selectList(wrapper);
    }

    /**
     * 执行通知任务
     */
    @Override
    @Transactional
    public void executeTask(Long taskId) {
        TongzhirenwuEntity task = tongzhirenwuService.selectById(taskId);
        if (task == null || !"待执行".equals(task.getZhuangtai())) {
            return;
        }

        // 创建就诊通知
        JiuzhentongzhiEntity tongzhi = new JiuzhentongzhiEntity();
        tongzhi.setTongzhibianhao(generateTongzhiBianhao());
        tongzhi.setYishengzhanghao(task.getYishengzhanghao());
        tongzhi.setDianhua(task.getDianhua());
        tongzhi.setJiuzhenshijian(task.getYuyueshijian());
        tongzhi.setTongzhishijian(new Date());
        tongzhi.setZhanghao(task.getZhanghao());
        tongzhi.setShouji(task.getShouji());
        tongzhi.setTongzhibeizhu(buildTaskTongzhiContent(task));
        tongzhi.setYuyueId(task.getYuyueId());
        tongzhi.setTongzhiLeixing(task.getTongzhiLeixing());
        tongzhi.setTongzhiZhuangtai("待发送");
        tongzhi.setFasonCishu(0);

        jiuzhentongzhiService.insert(tongzhi);

        // 发送通知
        boolean success = sendNotification(tongzhi);

        // 更新通知状态
        tongzhi.setTongzhiZhuangtai(success ? "已发送" : "发送失败");
        tongzhi.setFasonCishu(1);
        tongzhi.setZuizhongFasonShijian(new Date());
        jiuzhentongzhiService.updateById(tongzhi);

        // 记录结果
        recordNotificationResult(tongzhi, success, success ? null : "定时任务发送失败");

        // 更新任务状态
        task.setZhuangtai("已执行");
        tongzhirenwuService.updateById(task);
    }

    /**
     * 构建任务通知内容
     */
    private String buildTaskTongzhiContent(TongzhirenwuEntity task) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder content = new StringBuilder();

        switch (task.getTongzhiLeixing()) {
            case "就诊前24小时提醒":
                content.append("温馨提醒：您预约的就诊将在24小时后（").append(sdf.format(task.getYuyueshijian()))
                       .append("）开始，请做好准备。");
                break;
            case "就诊前1小时提醒":
                content.append("温馨提醒：您预约的就诊将在1小时后（").append(sdf.format(task.getYuyueshijian()))
                       .append("）开始，请准时到达。");
                break;
            case "就诊时间提醒":
                content.append("就诊提醒：您的就诊时间已到（").append(sdf.format(task.getYuyueshijian()))
                       .append("），请前往就诊。");
                break;
            default:
                content.append("就诊通知：").append(task.getTongzhiLeixing());
        }

        return content.toString();
    }

    /**
     * 取消预约相关的所有通知任务
     */
    @Override
    @Transactional
    public void cancelNotificationsByYuyueId(Long yuyueId) {
        // 取消待执行的任务
        EntityWrapper<TongzhirenwuEntity> taskWrapper = new EntityWrapper<>();
        taskWrapper.eq("yuyue_id", yuyueId).eq("zhuangtai", "待执行");

        List<TongzhirenwuEntity> tasks = tongzhirenwuService.selectList(taskWrapper);
        for (TongzhirenwuEntity task : tasks) {
            task.setZhuangtai("已取消");
            tongzhirenwuService.updateById(task);
        }
    }

    /**
     * 计算下次重试时间（指数退避策略）
     * 第1次：5分钟后
     * 第2次：10分钟后
     * 第3次：20分钟后
     */
    @Override
    public Date calculateNextRetryTime(int retryCount) {
        Calendar calendar = Calendar.getInstance();
        // 指数退避：5分钟 * 2^retryCount
        int minutes = RETRY_BASE_INTERVAL * (int) Math.pow(2, retryCount);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }
}
