package com.cl.dao;

import com.cl.entity.TongzhirenwuEntity;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;

import org.apache.ibatis.annotations.Param;
import com.cl.entity.view.TongzhirenwuView;


/**
 * 通知任务
 * 
 * @author 
 * @email 
 * @date 2025-03-27 15:44:15
 */
public interface TongzhirenwuDao extends BaseMapper<TongzhirenwuEntity> {
	
	List<TongzhirenwuView> selectListView(@Param("ew") Wrapper<TongzhirenwuEntity> wrapper);

	List<TongzhirenwuView> selectListView(Pagination page,@Param("ew") Wrapper<TongzhirenwuEntity> wrapper);
	
	TongzhirenwuView selectView(@Param("ew") Wrapper<TongzhirenwuEntity> wrapper);


}
