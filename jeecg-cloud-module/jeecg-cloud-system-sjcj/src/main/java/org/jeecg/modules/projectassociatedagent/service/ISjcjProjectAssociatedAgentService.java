package org.jeecg.modules.projectassociatedagent.service;

import org.jeecg.modules.projectassociatedagent.entity.SjcjProjectAssociatedAgent;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 项目与客户端关系表
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
public interface ISjcjProjectAssociatedAgentService extends IService<SjcjProjectAssociatedAgent> {
	// 根据项目ID及客户端ID获取其测试工具ID
	String getToolIdByCondition(String projectId, String sjcjAgentId);
}
