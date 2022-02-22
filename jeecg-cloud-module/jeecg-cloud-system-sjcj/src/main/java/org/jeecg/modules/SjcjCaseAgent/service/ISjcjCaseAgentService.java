package org.jeecg.modules.SjcjCaseAgent.service;

import org.jeecg.modules.SjcjCaseAgent.entity.SjcjCaseAgent;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ISjcjCaseAgentService extends IService<SjcjCaseAgent> {
	// 根据项目ID及客户端ID获取其测试工具ID
	String getToolIdByCondition(String projectId, String sjcjAgentId);
}
