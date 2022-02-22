package org.jeecg.modules.projectassociatedagent.mapper;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.projectassociatedagent.entity.SjcjProjectAssociatedAgent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 项目与客户端关系表
 * @Author: jeecg-boot
 * @Date: 2021-08-17
 * @Version: V1.0
 */
public interface SjcjProjectAssociatedAgentMapper extends BaseMapper<SjcjProjectAssociatedAgent> {
	// 根据项目ID及客户端ID获取其测试工具ID
	@Select("SELECT test_tool_id FROM sjcj_project_associated_agent WHERE project_id = #{projectId} AND sjcj_agent_id = #{sjcjAgentId}")
	String getToolIdByCondition(String projectId, String sjcjAgentId);
}
