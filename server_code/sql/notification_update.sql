-- 医院管理系统就诊通知功能升级SQL
-- 添加通知记录表和修改现有表结构

USE `cl515882190`;

-- 1. 修改就诊通知表，添加状态字段
ALTER TABLE `jiuzhentongzhi`
ADD COLUMN `tongzhi_zhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '待发送' COMMENT '通知状态：待发送、已发送、发送失败',
ADD COLUMN `fason_cishu` int(11) DEFAULT '0' COMMENT '发送次数',
ADD COLUMN `zuizhong_fason_shijian` datetime DEFAULT NULL COMMENT '最终发送时间',
ADD COLUMN `yuyue_id` bigint(20) DEFAULT NULL COMMENT '关联预约ID',
ADD COLUMN `tongzhi_leixing` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '就诊提醒' COMMENT '通知类型：预约成功、就诊提醒、就诊前24小时提醒、就诊前1小时提醒';

-- 2. 创建通知记录表（用于记录每次发送的详细情况）
DROP TABLE IF EXISTS `tongzhi_jilu`;
CREATE TABLE `tongzhi_jilu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `jiuzhentongzhi_id` bigint(20) NOT NULL COMMENT '就诊通知ID',
  `yuyue_id` bigint(20) DEFAULT NULL COMMENT '关联预约ID',
  `zhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `shouji` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `tongzhi_leixing` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知类型',
  `fason_shijian` datetime DEFAULT NULL COMMENT '发送时间',
  `fason_zhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送状态：成功、失败',
  `shibai_yuanyin` longtext COLLATE utf8mb4_unicode_ci COMMENT '失败原因',
  `chongshi_cishu` int(11) DEFAULT '0' COMMENT '重试次数',
  `xiaci_chongshi_shijian` datetime DEFAULT NULL COMMENT '下次重试时间',
  `chuli_zhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '未处理' COMMENT '处理状态：未处理、已处理、已忽略',
  `chuli_beizhu` longtext COLLATE utf8mb4_unicode_ci COMMENT '处理备注',
  `chuli_shijian` datetime DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`id`),
  KEY `jiuzhentongzhi_id` (`jiuzhentongzhi_id`),
  KEY `yuyue_id` (`yuyue_id`),
  KEY `fason_zhuangtai` (`fason_zhuangtai`),
  KEY `chuli_zhuangtai` (`chuli_zhuangtai`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知记录表';

-- 3. 创建通知任务表（用于存储待发送的通知任务）
DROP TABLE IF EXISTS `tongzhi_renwu`;
CREATE TABLE `tongzhi_renwu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `addtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `yuyue_id` bigint(20) NOT NULL COMMENT '关联预约ID',
  `zhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户账号',
  `shouji` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `yishengzhanghao` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医生账号',
  `dianhua` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '医生电话',
  `yuyueshijian` datetime DEFAULT NULL COMMENT '预约就诊时间',
  `tongzhi_leixing` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '通知类型',
  `jihua_fason_shijian` datetime DEFAULT NULL COMMENT '计划发送时间',
  `zhuangtai` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT '待执行' COMMENT '任务状态：待执行、已执行、已取消',
  `youxianji` int(11) DEFAULT '5' COMMENT '优先级：1-10，数字越小优先级越高',
  PRIMARY KEY (`id`),
  KEY `yuyue_id` (`yuyue_id`),
  KEY `zhuangtai` (`zhuangtai`),
  KEY `jihua_fason_shijian` (`jihua_fason_shijian`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知任务表';

-- 4. 添加菜单权限（管理员后台查看通知记录）
INSERT INTO `menu` (`id`, `addtime`, `menujson`, `role`, `parentid`) VALUES 
(NULL, NOW(), '{"name": "通知记录管理", "icon": "el-icon-message", "url": "tongzhijilu", "child": [
  {"name": "通知记录列表", "url": "views/tongzhijilu/list"},
  {"name": "通知任务列表", "url": "views/tongzhirenwu/list"}
]}', '管理员', 0);

-- 5. 添加管理员菜单
INSERT INTO `menu` (`id`, `addtime`, `menujson`, `role`, `parentid`) VALUES 
(NULL, NOW(), '{"name": "通知记录", "icon": "el-icon-message", "url": "tongzhijilu/list", "child": []}', '管理员', 0);
