package com.cl.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.lang.reflect.InvocationTargetException;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.beanutils.BeanUtils;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;


/**
 * 通知记录
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@TableName("tongzhi_jilu")
public class TongzhijiluEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public TongzhijiluEntity() {
		
	}
	
	public TongzhijiluEntity(T t) {
		try {
			BeanUtils.copyProperties(this, t);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 主键id
	 */
	@TableId(type = IdType.AUTO)
	private Long id;
	
	/**
	 * 就诊通知ID
	 */
				
	private Long jiuzhentongzhiId;
	
	/**
	 * 关联预约ID
	 */
				
	private Long yuyueId;
	
	/**
	 * 用户账号
	 */
				
	private String zhanghao;
	
	/**
	 * 手机号
	 */
				
	private String shouji;
	
	/**
	 * 通知类型
	 */
				
	private String tongzhiLeixing;
	
	/**
	 * 发送时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date fasonShijian;
	
	/**
	 * 发送状态：成功、失败
	 */
				
	private String fasonZhuangtai;
	
	/**
	 * 失败原因
	 */
				
	private String shibaiYuanyin;
	
	/**
	 * 重试次数
	 */
				
	private Integer chongshiCishu;
	
	/**
	 * 下次重试时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date xiaciChongshiShijian;
	
	/**
	 * 处理状态：未处理、已处理、已忽略
	 */
				
	private String chuliZhuangtai;
	
	/**
	 * 处理备注
	 */
				
	private String chuliBeizhu;
	
	/**
	 * 处理时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date chuliShijian;
	

	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat
	private Date addtime;

	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * 设置：就诊通知ID
	 */
	public void setJiuzhentongzhiId(Long jiuzhentongzhiId) {
		this.jiuzhentongzhiId = jiuzhentongzhiId;
	}
	/**
	 * 获取：就诊通知ID
	 */
	public Long getJiuzhentongzhiId() {
		return jiuzhentongzhiId;
	}
	
	/**
	 * 设置：关联预约ID
	 */
	public void setYuyueId(Long yuyueId) {
		this.yuyueId = yuyueId;
	}
	/**
	 * 获取：关联预约ID
	 */
	public Long getYuyueId() {
		return yuyueId;
	}
	
	/**
	 * 设置：用户账号
	 */
	public void setZhanghao(String zhanghao) {
		this.zhanghao = zhanghao;
	}
	/**
	 * 获取：用户账号
	 */
	public String getZhanghao() {
		return zhanghao;
	}
	
	/**
	 * 设置：手机号
	 */
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	/**
	 * 获取：手机号
	 */
	public String getShouji() {
		return shouji;
	}
	
	/**
	 * 设置：通知类型
	 */
	public void setTongzhiLeixing(String tongzhiLeixing) {
		this.tongzhiLeixing = tongzhiLeixing;
	}
	/**
	 * 获取：通知类型
	 */
	public String getTongzhiLeixing() {
		return tongzhiLeixing;
	}
	
	/**
	 * 设置：发送时间
	 */
	public void setFasonShijian(Date fasonShijian) {
		this.fasonShijian = fasonShijian;
	}
	/**
	 * 获取：发送时间
	 */
	public Date getFasonShijian() {
		return fasonShijian;
	}
	
	/**
	 * 设置：发送状态
	 */
	public void setFasonZhuangtai(String fasonZhuangtai) {
		this.fasonZhuangtai = fasonZhuangtai;
	}
	/**
	 * 获取：发送状态
	 */
	public String getFasonZhuangtai() {
		return fasonZhuangtai;
	}
	
	/**
	 * 设置：失败原因
	 */
	public void setShibaiYuanyin(String shibaiYuanyin) {
		this.shibaiYuanyin = shibaiYuanyin;
	}
	/**
	 * 获取：失败原因
	 */
	public String getShibaiYuanyin() {
		return shibaiYuanyin;
	}
	
	/**
	 * 设置：重试次数
	 */
	public void setChongshiCishu(Integer chongshiCishu) {
		this.chongshiCishu = chongshiCishu;
	}
	/**
	 * 获取：重试次数
	 */
	public Integer getChongshiCishu() {
		return chongshiCishu;
	}
	
	/**
	 * 设置：下次重试时间
	 */
	public void setXiaciChongshiShijian(Date xiaciChongshiShijian) {
		this.xiaciChongshiShijian = xiaciChongshiShijian;
	}
	/**
	 * 获取：下次重试时间
	 */
	public Date getXiaciChongshiShijian() {
		return xiaciChongshiShijian;
	}
	
	/**
	 * 设置：处理状态
	 */
	public void setChuliZhuangtai(String chuliZhuangtai) {
		this.chuliZhuangtai = chuliZhuangtai;
	}
	/**
	 * 获取：处理状态
	 */
	public String getChuliZhuangtai() {
		return chuliZhuangtai;
	}
	
	/**
	 * 设置：处理备注
	 */
	public void setChuliBeizhu(String chuliBeizhu) {
		this.chuliBeizhu = chuliBeizhu;
	}
	/**
	 * 获取：处理备注
	 */
	public String getChuliBeizhu() {
		return chuliBeizhu;
	}
	
	/**
	 * 设置：处理时间
	 */
	public void setChuliShijian(Date chuliShijian) {
		this.chuliShijian = chuliShijian;
	}
	/**
	 * 获取：处理时间
	 */
	public Date getChuliShijian() {
		return chuliShijian;
	}

}
