package org.jeecg.modules.message.job;

import java.util.List;

import org.jeecg.modules.client.entity.RunningClient;
import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 发送消息任务
 */

@Slf4j
/**
 * @Author: test
 * */
public class GetKlocProjectName implements Job {

	@Autowired
	private IRunningClientService clientService;
	
	@Autowired
	private TaskDispatcher taskDis;
	
	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		//获取kloc的服务端
		QueryWrapper<RunningClient> clientWrapper = new QueryWrapper<>();
		clientWrapper.eq("tool_name", "klocwork").eq("client_state", 1);
		List<RunningClient> clientList = clientService.list();
		
		//循环更新
		clientList.forEach(item->{
			taskDis.getKlocProjectNames(item.getClientIp());
		});
		
	}

}
