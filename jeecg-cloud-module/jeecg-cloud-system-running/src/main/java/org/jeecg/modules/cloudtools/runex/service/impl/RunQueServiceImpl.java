package org.jeecg.modules.cloudtools.runex.service.impl;

import java.util.Date;

import org.jeecg.modules.cloudtools.runex.entity.RunCase;
import org.jeecg.modules.cloudtools.runex.entity.RunQue;
import org.jeecg.modules.cloudtools.runex.mapper.RunQueMapper;
import org.jeecg.modules.cloudtools.runex.service.IRunCaseService;
import org.jeecg.modules.cloudtools.runex.service.IRunQueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * @Description: kloc队列表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Service
public class RunQueServiceImpl extends ServiceImpl<RunQueMapper, RunQue> implements IRunQueService {

	@Autowired
	private IRunCaseService caseService;
	
	@Override
	public void removeByCaseId(String caseId) {
		
		QueryWrapper<RunQue> wrapper = new QueryWrapper<>();
		wrapper.eq("case_id", caseId);
		
		RunCase casese = caseService.getById(caseId);
		casese.setEndTime(new Date());
		caseService.updateById(casese);
		
		this.remove(wrapper);
		
	}

	
	@Override
	public void updateStep(String caseId, String step) {
		
		RunQue que = getByCaseId(caseId);
		que.setExecStep(step);
		this.updateById(que);
		
	}


	@Override
	public RunQue getByCaseId(String id) {
		UpdateWrapper<RunQue> wrapper = new UpdateWrapper<>();
		wrapper.eq("case_id", id);
		RunQue que =this.getOne(wrapper, false);
		return que;
	}


	@Override
	public void updateStatus(String id, String status) {
		// TODO Auto-generated method stub
		
	}

}
