-- 更新就诊通知表结构，添加通知状态、重试次数、失败原因等字段

ALTER TABLE `jiuzhentongzhi` 
ADD COLUMN `tongzhizhuangtai` INT DEFAULT 0 COMMENT '通知状态：0-待发送，1-已发送，2-发送失败' AFTER `tongzhibeizhu`,
ADD COLUMN `chongcishu` INT DEFAULT 0 COMMENT '重试次数' AFTER `tongzhizhuangtai`,
ADD COLUMN `shibaiyuanyin` VARCHAR(500) DEFAULT NULL COMMENT '失败原因' AFTER `chongcishu`,
ADD COLUMN `zuijiafashijian` DATETIME DEFAULT NULL COMMENT '最后发送时间' AFTER `shibaiyuanyin`;
