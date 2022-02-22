package org.jeecg.modules.cloud.service;


import com.baomidou.mybatisplus.extension.service.IService;

import org.jeecg.modules.cloud.entity.RunningCloudKlocCase;

/**
 * @Description: kloc用例表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
public interface IRunningCloudKlocCaseService extends IService<RunningCloudKlocCase> {
	/**
	 * 更新数据
	 * @param id true
	 * @param status true
	 * */
	public void updateStatus(String id ,String status);
	/**
	 * 更新步骤
	 * @param id true
	 * @param step true
	 * */
	public void updateStep(String id ,String step);

	
}
