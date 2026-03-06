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
 * 通知任务
 * 数据库通用操作实体类（普通增删改查）
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@TableName("tongzhi_renwu")
public class TongzhirenwuEntity<T> implements Serializable {
	private static final long serialVersionUID = 1L;


	public TongzhirenwuEntity() {
		
	}
	
	public TongzhirenwuEntity(T t) {
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
	 * 医生账号
	 */
				
	private String yishengzhanghao;
	
	/**
	 * 医生电话
	 */
				
	private String dianhua;
	
	/**
	 * 预约就诊时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date yuyueshijian;
	
	/**
	 * 通知类型
	 */
				
	private String tongzhiLeixing;
	
	/**
	 * 计划发送时间
	 */
				
	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat 		
	private Date jihuaFasonShijian;
	
	/**
	 * 任务状态：待执行、已执行、已取消
	 */
				
	private String zhuangtai;
	
	/**
	 * 优先级：1-10，数字越小优先级越高
	 */
				
	private Integer youxianji;
	

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
	 * 设置：医生账号
	 */
	public void setYishengzhanghao(String yishengzhanghao) {
		this.yishengzhanghao = yishengzhanghao;
	}
	/**
	 * 获取：医生账号
	 */
	public String getYishengzhanghao() {
		return yishengzhanghao;
	}
	
	/**
	 * 设置：医生电话
	 */
	public void setDianhua(String dianhua) {
		this.dianhua = dianhua;
	}
	/**
	 * 获取：医生电话
	 */
	public String getDianhua() {
		return dianhua;
	}
	
	/**
	 * 设置：预约就诊时间
	 */
	public void setYuyueshijian(Date yuyueshijian) {
		this.yuyueshijian = yuyueshijian;
	}
	/**
	 * 获取：预约就诊时间
	 */
	public Date getYuyueshijian() {
		return yuyueshijian;
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
	 * 设置：计划发送时间
	 */
	public void setJihuaFasonShijian(Date jihuaFasonShijian) {
		this.jihuaFasonShijian = jihuaFasonShijian;
	}
	/**
	 * 获取：计划发送时间
	 */
	public Date getJihuaFasonShijian() {
		return jihuaFasonShijian;
	}
	
	/**
	 * 设置：任务状态
	 */
	public void setZhuangtai(String zhuangtai) {
		this.zhuangtai = zhuangtai;
	}
	/**
	 * 获取：任务状态
	 */
	public String getZhuangtai() {
		return zhuangtai;
	}
	
	/**
	 * 设置：优先级
	 */
	public void setYouxianji(Integer youxianji) {
		this.youxianji = youxianji;
	}
	/**
	 * 获取：优先级
	 */
	public Integer getYouxianji() {
		return youxianji;
	}

}
