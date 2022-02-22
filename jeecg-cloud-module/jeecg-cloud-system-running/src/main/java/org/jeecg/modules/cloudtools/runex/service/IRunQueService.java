package org.jeecg.modules.cloudtools.runex.service;

import org.jeecg.modules.cloudtools.runex.entity.RunQue;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 记录xrun的用例
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
public interface IRunQueService extends IService<RunQue> {
	public void removeByCaseId(String caseId);
	public void updateStep(String caseId ,String step);
	public RunQue getByCaseId(String id);
	
	public void updateStatus(String id ,String status);
	
	
}
