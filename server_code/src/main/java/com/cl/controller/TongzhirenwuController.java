package com.cl.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import com.cl.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.cl.annotation.IgnoreAuth;
import com.cl.annotation.SysLog;

import com.cl.entity.TongzhirenwuEntity;
import com.cl.entity.view.TongzhirenwuView;

import com.cl.service.TongzhirenwuService;
import com.cl.service.TokenService;
import com.cl.utils.PageUtils;
import com.cl.utils.R;
import com.cl.utils.MPUtil;
import com.cl.utils.MapUtils;
import com.cl.utils.CommonUtil;

/**
 * 通知任务
 * 后端接口
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
@RestController
@RequestMapping("/tongzhirenwu")
public class TongzhirenwuController {
    @Autowired
    private TongzhirenwuService tongzhirenwuService;









    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,TongzhirenwuEntity tongzhirenwu,
                                                                                                                                                HttpServletRequest request){
        EntityWrapper<TongzhirenwuEntity> ew = new EntityWrapper<TongzhirenwuEntity>();
																								
        
        
        PageUtils page = tongzhirenwuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhirenwu), params), params));
        return R.ok().put("data", page);
    }







    /**
     * 前端列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,TongzhirenwuEntity tongzhirenwu,
		HttpServletRequest request){
        EntityWrapper<TongzhirenwuEntity> ew = new EntityWrapper<TongzhirenwuEntity>();

		PageUtils page = tongzhirenwuService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, tongzhirenwu), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( TongzhirenwuEntity tongzhirenwu){
       	EntityWrapper<TongzhirenwuEntity> ew = new EntityWrapper<TongzhirenwuEntity>();
      	ew.allEq(MPUtil.allEQMapPre( tongzhirenwu, "tongzhirenwu")); 
        return R.ok().put("data", tongzhirenwuService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(TongzhirenwuEntity tongzhirenwu){
        EntityWrapper< TongzhirenwuEntity> ew = new EntityWrapper< TongzhirenwuEntity>();
 		ew.allEq(MPUtil.allEQMapPre( tongzhirenwu, "tongzhirenwu")); 
		TongzhirenwuView tongzhirenwuView =  tongzhirenwuService.selectView(ew);
		return R.ok("查询通知任务成功").put("data", tongzhirenwuView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        TongzhirenwuEntity tongzhirenwu = tongzhirenwuService.selectById(id);
		tongzhirenwu = tongzhirenwuService.selectView(new EntityWrapper<TongzhirenwuEntity>().eq("id", id));
        return R.ok().put("data", tongzhirenwu);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        TongzhirenwuEntity tongzhirenwu = tongzhirenwuService.selectById(id);
		tongzhirenwu = tongzhirenwuService.selectView(new EntityWrapper<TongzhirenwuEntity>().eq("id", id));
        return R.ok().put("data", tongzhirenwu);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @SysLog("新增通知任务")
    public R save(@RequestBody TongzhirenwuEntity tongzhirenwu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(tongzhirenwu);
        tongzhirenwuService.insert(tongzhirenwu);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @SysLog("新增通知任务")
    @RequestMapping("/add")
    public R add(@RequestBody TongzhirenwuEntity tongzhirenwu, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(tongzhirenwu);
        tongzhirenwuService.insert(tongzhirenwu);
        return R.ok();
    }



    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @SysLog("修改通知任务")
    public R update(@RequestBody TongzhirenwuEntity tongzhirenwu, HttpServletRequest request){
        //ValidatorUtils.validateEntity(tongzhirenwu);
        tongzhirenwuService.updateById(tongzhirenwu);//全部更新
        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @SysLog("删除通知任务")
    public R delete(@RequestBody Long[] ids){
        tongzhirenwuService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	








}
