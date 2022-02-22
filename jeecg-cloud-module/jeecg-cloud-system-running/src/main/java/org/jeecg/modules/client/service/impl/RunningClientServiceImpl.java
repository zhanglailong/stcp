package org.jeecg.modules.client.service.impl;

import org.jeecg.modules.client.service.IRunningClientService;
import org.jeecg.modules.client.entity.RunningClient;
import org.jeecg.modules.client.mapper.RunningClientMapper;
import org.jeecg.modules.task.dispatch.TaskDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 云化工具客户端
 * @Author: jeecg-boot
 * @Date:   2020-12-23
 * @Version: V1.0
 */
@Service
public class RunningClientServiceImpl extends ServiceImpl<RunningClientMapper, RunningClient> implements IRunningClientService {

	@Autowired
	private TaskDispatcher taskDispatcher;
	
	@Override
	public void changeState(String ip,String state) {
		
		
		//获取客户端实体
		String sign1="1";
		String sign0="0";
		String klocwork="klocwork";
		RunningClient runningClient;
		QueryWrapper<RunningClient> wrapper = new QueryWrapper<>();
		wrapper.eq("client_ip", ip);
		runningClient = this.getOne(wrapper);
		//若实体不存在
		if(runningClient == null) {
			runningClient = new RunningClient();
			runningClient.setClientIp(ip);
		}else if(sign1.equals(state) && sign0.equals(runningClient.getClientState())){
			//实体存在且为新连接，则更新内容
			if(klocwork.equals(runningClient.getToolName())) {
				taskDispatcher.getKlocProjectNames(ip);
			}
		}
		
		//修改状态
		if(!state.equals(runningClient.getClientState())) {
			runningClient.setClientState(state);
			this.saveOrUpdate(runningClient);
		}
		
	}
	@Override
	public RunningClient getByIp(String ip) {
		
		QueryWrapper<RunningClient> qw = new QueryWrapper<>();
		qw.eq("client_ip",ip);
		return this.getOne(qw,false);
		
	}

}
