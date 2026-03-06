package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.TongzhirenwuDao;
import com.cl.entity.TongzhirenwuEntity;
import com.cl.service.TongzhirenwuService;
import com.cl.entity.view.TongzhirenwuView;

@Service("tongzhirenwuService")
public class TongzhirenwuServiceImpl extends ServiceImpl<TongzhirenwuDao, TongzhirenwuEntity> implements TongzhirenwuService {

    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<TongzhirenwuEntity> page = this.selectPage(
                new Query<TongzhirenwuEntity>(params).getPage(),
                new EntityWrapper<TongzhirenwuEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<TongzhirenwuEntity> wrapper) {
		  Page<TongzhirenwuView> page =new Query<TongzhirenwuView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<TongzhirenwuView> selectListView(Wrapper<TongzhirenwuEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public TongzhirenwuView selectView(Wrapper<TongzhirenwuEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	


}
