package com.cl.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.UUID;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cl.utils.PageUtils;
import com.cl.utils.Query;


import com.cl.dao.JiuzhentongzhiDao;
import com.cl.entity.JiuzhentongzhiEntity;
import com.cl.entity.YishengyuyueEntity;
import com.cl.service.JiuzhentongzhiService;
import com.cl.entity.view.JiuzhentongzhiView;

@Service("jiuzhentongzhiService")
public class JiuzhentongzhiServiceImpl extends ServiceImpl<JiuzhentongzhiDao, JiuzhentongzhiEntity> implements JiuzhentongzhiService {

    private static final int MAX_RETRY_TIMES = 3;
    	
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<JiuzhentongzhiEntity> page = this.selectPage(
                new Query<JiuzhentongzhiEntity>(params).getPage(),
                new EntityWrapper<JiuzhentongzhiEntity>()
        );
        return new PageUtils(page);
    }
    
    @Override
	public PageUtils queryPage(Map<String, Object> params, Wrapper<JiuzhentongzhiEntity> wrapper) {
		  Page<JiuzhentongzhiView> page =new Query<JiuzhentongzhiView>(params).getPage();
	        page.setRecords(baseMapper.selectListView(page,wrapper));
	    	PageUtils pageUtil = new PageUtils(page);
	    	return pageUtil;
 	}
    
	@Override
	public List<JiuzhentongzhiView> selectListView(Wrapper<JiuzhentongzhiEntity> wrapper) {
		return baseMapper.selectListView(wrapper);
	}

	@Override
	public JiuzhentongzhiView selectView(Wrapper<JiuzhentongzhiEntity> wrapper) {
		return baseMapper.selectView(wrapper);
	}
	
	@Override
	public void createNotifications(YishengyuyueEntity yuyue) {
		Date now = new Date();
		
		String[] reminderTypes = {"预约成功通知", "就诊前1天提醒", "就诊当天提醒"};
		String[] reminderNotes = {"您的预约已成功确认", "明天就是您的就诊时间了，请做好准备", "今天是您的就诊时间，请准时就诊"};
		
		for (int i = 0; i < reminderTypes.length; i++) {
			JiuzhentongzhiEntity tongzhi = new JiuzhentongzhiEntity();
			tongzhi.setTongzhibianhao(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
			tongzhi.setYishengzhanghao(yuyue.getYishengzhanghao());
			tongzhi.setDianhua(yuyue.getDianhua());
			tongzhi.setJiuzhenshijian(yuyue.getYuyueshijian());
			tongzhi.setTongzhishijian(now);
			tongzhi.setZhanghao(yuyue.getZhanghao());
			tongzhi.setShouji(yuyue.getShouji());
			tongzhi.setTongzhibeizhu(reminderNotes[i] + " - " + reminderTypes[i]);
			tongzhi.setTongzhizhuangtai(0);
			tongzhi.setChongcishu(0);
			tongzhi.setAddtime(now);
			
			this.insert(tongzhi);
			
			sendNotification(tongzhi);
		}
	}
	
	@Override
	public boolean sendNotification(JiuzhentongzhiEntity tongzhi) {
		Date now = new Date();
		tongzhi.setZuijiafashijian(now);
		
		try {
			boolean sendSuccess = doSendNotification(tongzhi);
			
			if (sendSuccess) {
				tongzhi.setTongzhizhuangtai(1);
				tongzhi.setShibaiyuanyin(null);
			} else {
				tongzhi.setTongzhizhuangtai(2);
				tongzhi.setChongcishu(tongzhi.getChongcishu() + 1);
				tongzhi.setShibaiyuanyin("发送失败");
			}
			
			this.updateById(tongzhi);
			return sendSuccess;
		} catch (Exception e) {
			tongzhi.setTongzhizhuangtai(2);
			tongzhi.setChongcishu(tongzhi.getChongcishu() + 1);
			tongzhi.setShibaiyuanyin(e.getMessage());
			this.updateById(tongzhi);
			return false;
		}
	}
	
	private boolean doSendNotification(JiuzhentongzhiEntity tongzhi) {
		System.out.println("发送通知: " + tongzhi.getTongzhibeizhu() + " 到手机: " + tongzhi.getShouji());
		return true;
	}
	
	@Override
	public void retryFailedNotifications() {
		EntityWrapper<JiuzhentongzhiEntity> wrapper = new EntityWrapper<>();
		wrapper.eq("tongzhizhuangtai", 2);
		wrapper.lt("chongcishu", MAX_RETRY_TIMES);
		
		List<JiuzhentongzhiEntity> failedList = this.selectList(wrapper);
		
		for (JiuzhentongzhiEntity tongzhi : failedList) {
			sendNotification(tongzhi);
		}
	}
	
	@Override
	public void manualRetry(Long id) {
		JiuzhentongzhiEntity tongzhi = this.selectById(id);
		if (tongzhi != null) {
			sendNotification(tongzhi);
		}
	}
	


}
