package org.jeecg.modules.projectassociatedagent.service.impl;

import org.jeecg.modules.projectassociatedagent.entity.SjcjProjectAssociatedAgent;
import org.jeecg.modules.projectassociatedagent.mapper.SjcjProjectAssociatedAgentMapper;
import org.jeecg.modules.projectassociatedagent.service.ISjcjProjectAssociatedAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 项目与客户端关系表
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
@Service
public class SjcjProjectAssociatedAgentServiceImpl
		extends ServiceImpl<SjcjProjectAssociatedAgentMapper, SjcjProjectAssociatedAgent>
		implements ISjcjProjectAssociatedAgentService {

	@Autowired
	SjcjProjectAssociatedAgentMapper sjcjProjectAssociatedAgentMapper;

	@Override
	public String getToolIdByCondition(String projectId, String sjcjAgentId) {
		return sjcjProjectAssociatedAgentMapper.getToolIdByCondition(projectId, sjcjAgentId);
	}

}
