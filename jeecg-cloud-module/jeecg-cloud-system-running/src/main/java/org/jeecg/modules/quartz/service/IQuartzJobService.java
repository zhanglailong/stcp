package org.jeecg.modules.quartz.service;

import java.util.List;

import org.jeecg.modules.quartz.entity.QuartzJob;
import org.quartz.SchedulerException;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 定时任务在线管理
 * @Author: jeecg-boot
 * @Date: 2019-04-28
 * @Version: V1.1
 */
public interface IQuartzJobService extends IService<QuartzJob> {
	/**
	 * 获取ClassName
	 * @param jobClassName 文件流
	 * @return 没有返回值
	 * */
	List<QuartzJob> findByJobClassName(String jobClassName);
	/**
	 * 保存ClassName
	 * @param quartzJob 文件流
	 * @return 没有返回值
	 * */
	boolean saveAndScheduleJob(QuartzJob quartzJob);
	/**
	 * 编辑ClassName
	 * @param quartzJob 文件流
	 * @throws SchedulerException
	 * @return 没有返回值
	 * */
	boolean editAndScheduleJob(QuartzJob quartzJob) throws SchedulerException;
	/**
	 * 删除ClassName
	 * @param quartzJob 文件流
	 * @return 没有返回值
	 * */
	boolean deleteAndStopJob(QuartzJob quartzJob);
	/**
	 * 重置ClassName
	 * @param quartzJob 文件流
	 * @return 没有返回值
	 * */
	boolean resumeJob(QuartzJob quartzJob);

	/**
	 * 执行定时任务
	 * @param quartzJob
	 * @throws Exception
	 * @return 没有返回值
	 */
	void execute(QuartzJob quartzJob) throws Exception;

	/**
	 * 暂停任务
	 * @param quartzJob
	 * @return 没有返回值
	 */
	void pause(QuartzJob quartzJob);
}
