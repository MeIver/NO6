package com.cl.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cl.utils.PageUtils;
import com.cl.entity.TongzhirenwuEntity;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.TongzhirenwuView;


/**
 * 通知任务
 *
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
public interface TongzhirenwuService extends IService<TongzhirenwuEntity> {

    PageUtils queryPage(Map<String, Object> params);
   
   	List<TongzhirenwuView> selectListView(Wrapper<TongzhirenwuEntity> wrapper);
   	
   	TongzhirenwuView selectView(@Param("ew") Wrapper<TongzhirenwuEntity> wrapper);
   	
   	PageUtils queryPage(Map<String, Object> params,Wrapper<TongzhirenwuEntity> wrapper);
   	
   
}
