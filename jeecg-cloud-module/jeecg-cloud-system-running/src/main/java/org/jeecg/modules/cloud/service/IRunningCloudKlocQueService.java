package org.jeecg.modules.cloud.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cloud.entity.RunningCloudKlocQue;

/**
 * @Description: kloc队列表
 * @Author: jeecg-boot
 * @Date:   2021-04-08
 * @Version: V1.0
 */
public interface IRunningCloudKlocQueService extends IService<RunningCloudKlocQue> {
	/**
	 * 删除
	 * @param caseId true
	 * */
	public void removeByCaseId(String caseId);
	/**
	 * 更新
	 * @param caseId true
	 * @param step true
	 * */
	public void updateStep(String caseId ,String step);
	/**
	 * 更新
	 * @param id true
	 * @return 没有返回值
	 * */
	public RunningCloudKlocQue getByCaseId(String id);
}
