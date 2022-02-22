package org.jeecg.modules.SjcjCaseAgent.service.impl;

import org.jeecg.modules.SjcjCaseAgent.entity.SjcjCaseAgent;
import org.jeecg.modules.SjcjCaseAgent.mapper.SjcjCaseAgentMapper;
import org.jeecg.modules.SjcjCaseAgent.service.ISjcjCaseAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

@Service
public class SjcjCaseAgentServiceImpl extends ServiceImpl<SjcjCaseAgentMapper, SjcjCaseAgent>
		implements ISjcjCaseAgentService {

	@Autowired
	SjcjCaseAgentMapper sjcjCaseAgentMapper;

	@Override
	public String getToolIdByCondition(String projectId, String sjcjAgentId) {
		return sjcjCaseAgentMapper.getToolIdByCondition(projectId, sjcjAgentId);
	}

}
