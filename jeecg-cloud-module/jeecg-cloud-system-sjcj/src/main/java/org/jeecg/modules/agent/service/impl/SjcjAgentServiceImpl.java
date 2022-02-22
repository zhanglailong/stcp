package org.jeecg.modules.agent.service.impl;

import java.util.List;

import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.agent.entity.ServerStatusEnum;
import org.jeecg.modules.agent.entity.SjcjAgent;
import org.jeecg.modules.agent.mapper.SjcjAgentMapper;
import org.jeecg.modules.agent.service.ISjcjAgentService;
import org.jeecg.modules.running.entity.RunningProject;
import org.jeecg.modules.running.entity.RunningTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SjcjAgentServiceImpl extends ServiceImpl<SjcjAgentMapper, SjcjAgent> implements ISjcjAgentService {
	@Autowired
	SjcjAgentMapper sjcjAgentMapper;

	@Override
	public List<RunningProject> getTaskByProject(String projectId) {
		return sjcjAgentMapper.getTaskByProject(projectId);
	}

	@Override
	public List<String> getTaskIdByProject(String projectId) {
		return sjcjAgentMapper.getTaskIdByProject(projectId);
	}

	@Override
	public List<RunningTask> getCaseByTask(String taskId) {
		return sjcjAgentMapper.getCaseByTask(taskId);
	}

	@Override
	public int changeAgentStatusByEnvironmentId(String environmentId, String agentStatus) {
		return sjcjAgentMapper.changeAgentStatusByEnvironmentId(environmentId, agentStatus);
	}

	@Override
	public int changeAgentStatusByAgentIp(String agentIp, String agentStatus) {
		return sjcjAgentMapper.changeAgentStatusByAgentIp(agentIp, agentStatus);
	}

	@Override
	public List<RunningProject> getTreeByProjectId(String projectId) {
		return sjcjAgentMapper.getTreeByProjectId(projectId);
	}

	@Override
	public Page<SjcjAgent> queryPageList(Page<SjcjAgent> page, String projectId, SjcjAgent sjcjAgent) {
		List<SjcjAgent> list = sjcjAgentMapper.getAgentByProjectId(projectId, sjcjAgent);
		if (list.size() > 0) {
			for (SjcjAgent temp : list) {
				temp.setAgentStatus(ServerStatusEnum.toEnum(temp.getAgentStatus()).getDesc());
			}
		}
		return page.setRecords(list);
	}

	@Override
	public List<DictModel> getOptionByCondition(String projectId, String turnId, String testTypeId, String taskId) {
		return sjcjAgentMapper.getOptionByCondition(projectId, turnId, testTypeId, taskId);
	}

	@Override
	public SjcjAgent getSjcjAgentByCondition(String sjcjAgentBindCaseId) {
		return sjcjAgentMapper.getSjcjAgentByCondition(sjcjAgentBindCaseId);
	}
}
