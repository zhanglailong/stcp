package org.jeecg.modules.SjcjCaseAgent.mapper;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.SjcjCaseAgent.entity.SjcjCaseAgent;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

public interface SjcjCaseAgentMapper extends BaseMapper<SjcjCaseAgent> {

	// 根据项目ID及客户端ID获取其测试工具ID
	@Select("SELECT tool_id FROM sjcj_case_agent WHERE project_id = #{projectId} AND sjcj_agent_id = #{sjcjAgentId}")
	String getToolIdByCondition(String projectId, String sjcjAgentId);

}
