package org.jeecg.modules.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;

import org.jeecg.modules.cloud.mapper.RunningCloudKlocQueMapper;
import org.jeecg.modules.cloud.service.IRunningCloudKlocCaseService;
import org.jeecg.modules.cloud.service.IRunningCloudKlocQueService;
import org.jeecg.modules.cloud.entity.RunningCloudKlocCase;
import org.jeecg.modules.cloud.entity.RunningCloudKlocQue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description: kloc队列表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Service("klocQueService")
public class RunningCloudKlocQueServiceImpl extends ServiceImpl<RunningCloudKlocQueMapper, RunningCloudKlocQue> implements IRunningCloudKlocQueService {

	@Autowired
	private IRunningCloudKlocCaseService caseService;
	
	@Override
	public void removeByCaseId(String caseId) {
		
		QueryWrapper<RunningCloudKlocQue> wrapper = new QueryWrapper<>();
		wrapper.eq("kloc_case_id", caseId);
		
		RunningCloudKlocCase casese = caseService.getById(caseId);
		casese.setEndTime(new Date());
		caseService.updateById(casese);
		
		this.remove(wrapper);
		
	}

	
	@Override
	public void updateStep(String caseId, String step) {
		
		RunningCloudKlocQue que = getByCaseId(caseId);
		que.setExecStep(step);
		this.updateById(que);
		
	}


	@Override
	public RunningCloudKlocQue getByCaseId(String id) {
		UpdateWrapper<RunningCloudKlocQue> wrapper = new UpdateWrapper<>();
		wrapper.eq("kloc_case_id", id);
		RunningCloudKlocQue que =this.getOne(wrapper, false);
		return que;
	}

}
