package org.jeecg.modules.cloudtools.runex.service;

import org.jeecg.modules.cloudtools.runex.entity.RunCase;
import org.jeecg.modules.cloudtools.runex.entity.RunQue;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 记录xrun的用例
 * @Author: jeecg-boot
 * @Date:   2021-03-16
 * @Version: V1.0
 */
public interface IRunCaseService extends IService<RunCase> {

	public void updateStatus(String id ,String status);
	
	public void updateStep(String id ,String step);

}
