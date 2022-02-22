package org.jeecg.modules.cloudtools.runex.service.impl;

import java.util.Date;

import org.jeecg.modules.cloudtools.runex.entity.RunCase;
import org.jeecg.modules.cloudtools.runex.entity.RunQue;
import org.jeecg.modules.cloudtools.runex.mapper.RunCaseMapper;
import org.jeecg.modules.cloudtools.runex.service.IRunCaseService;
import org.jeecg.modules.cloudtools.runex.service.IRunQueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

/**
 * @Description: 记录xrun的用例
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
@Service
public class RunCaseServiceImpl extends ServiceImpl<RunCaseMapper, RunCase> implements IRunCaseService {
	
	@Autowired
	private IRunCaseService runCaseService;
	
	@Autowired
	private IRunQueService runQueService;
	
	
	@Override
	public boolean save(RunCase xc) {
		xc.setParams(xc.getParamsDetail().toJSONString());
		return super.save(xc);
	}
	
	@Override
	public boolean updateById(RunCase entity) {
		if(entity.getParamsDetail()!=null) {
			entity.setParams(entity.getParamsDetail().toJSONString());
		}
        return SqlHelper.retBool(getBaseMapper().updateById(entity));
    }
	
	
	@Override
	public void updateStatus(String id, String status) {
		
		RunCase klocCase = this.getById(id);
		klocCase.setStatus(status);
		
		if("1".equals(status)) {
			klocCase.setStartTime(new Date());
			klocCase.setEndTime(null);
			RunQue que =  runQueService.getByCaseId(id);
			que.setStartTime(new Date());
			runQueService.updateById(que);
		}
		
		this.updateById(klocCase);
	}

	@Override
	public void updateStep(String id, String step) {
		// TODO Auto-generated method stub
		RunCase klocCase = this.getById(id);
		klocCase.setExecStep(step);
		this.updateById(klocCase);
	}
	
}
