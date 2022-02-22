package org.jeecg.modules.agent.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.modules.agent.entity.SjcjAgent;
import org.jeecg.modules.running.entity.RunningProject;
import org.jeecg.modules.running.entity.RunningTask;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import feign.Param;

public interface SjcjAgentMapper extends BaseMapper<SjcjAgent> {
	// 根据项目ID获取项目与测试项层级列表
	List<RunningProject> getTaskByProject(String projectId);

	// 根据项目ID获取测试项ID列表
	@Select("SELECT id FROM running_task WHERE del_flag = 0 AND project_id = #{projectId}")
	List<String> getTaskIdByProject(String projectId);

	// 根据测试项ID获取测试项与测试用例层级列表
	List<RunningTask> getCaseByTask(String taskId);

	// 根据环境ID更改客户端状态
	@Update("UPDATE sjcj_agent SET agent_status = #{agentStatus} "
			+ "WHERE agent_ip IN ( SELECT sa.agent_ip FROM ( SELECT agent_ip "
			+ "FROM sjcj_agent WHERE environment_id = #{environmentId} ) sa )")
	int changeAgentStatusByEnvironmentId(String environmentId, String agentStatus);

	// 根据客户端IP更改客户端状态
	@Update("UPDATE sjcj_agent SET agent_status = #{agentStatus} WHERE agent_ip = #{agentIp}")
	int changeAgentStatusByAgentIp(String agentIp, String agentStatus);

	// 根据项目ID获取树层级列表(项目-轮次-测试类型-测试项-测试用例)
	List<RunningProject> getTreeByProjectId(String projectId);

	// 根据项目ID获取客户端列表
	List<SjcjAgent> getAgentByProjectId(@Param("projectId") String projectId, @Param("sjcjAgent") SjcjAgent sjcjAgent);

	// 下拉选层级列表
	List<DictModel> getOptionByCondition(String projectId, String turnId, String testTypeId, String taskId);

	// 根据`sjcj_agent_bind_case`表ID获取`sjcj_agent`表实体对象
	@Select("SELECT * FROM sjcj_agent WHERE id = (SELECT sjcj_agent_id FROM sjcj_agent_bind_case WHERE id = #{sjcjAgentBindCaseId})")
	SjcjAgent getSjcjAgentByCondition(String sjcjAgentBindCaseId);
}
