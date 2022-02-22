package org.jeecg.modules.cloud.service.impl;

import java.util.Date;

import org.jeecg.modules.cloud.mapper.RunningCloudKlocCaseMapper;
import org.jeecg.modules.cloud.service.IRunningCloudKlocCaseService;
import org.jeecg.modules.cloud.service.IRunningCloudKlocQueService;
import org.jeecg.modules.cloud.entity.RunningCloudKlocCase;
import org.jeecg.modules.cloud.entity.RunningCloudKlocQue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: kloc用例表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
@Service("klocCaseService")
public class RunningCloudKlocCaseServiceImpl extends ServiceImpl<RunningCloudKlocCaseMapper, RunningCloudKlocCase> implements IRunningCloudKlocCaseService {

	@Autowired
	private IRunningCloudKlocQueService klocqueService;
	
	@Override
	public void updateStatus(String id, String status) {
		
		RunningCloudKlocCase klocCase = this.getById(id);
		klocCase.setStatus(status);
		String sign1="1";
		if(sign1.equals(status)) {
			klocCase.setStartTime(new Date());
			klocCase.setEndTime(null);
			RunningCloudKlocQue que =  klocqueService.getByCaseId(id);
			que.setStartTime(new Date());
			klocqueService.updateById(que);
		}
		
		this.updateById(klocCase);
	}

	@Override
	public void updateStep(String id, String step) {
		// TODO Auto-generated method stub
		RunningCloudKlocCase klocCase = this.getById(id);
		klocCase.setExecStep(step);
		this.updateById(klocCase);
	}

}
