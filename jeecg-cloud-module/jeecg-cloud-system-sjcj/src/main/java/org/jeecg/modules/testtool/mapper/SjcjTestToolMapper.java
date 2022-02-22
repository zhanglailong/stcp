package org.jeecg.modules.testtool.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.testtool.entity.SjcjTestTool;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 测试工具采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-10
 * @Version: V1.0
 */
public interface SjcjTestToolMapper extends BaseMapper<SjcjTestTool> {
	// 根据sjcjAgentBindCaseId及测试工具ID获取测试工具采集配置
	@Select("SELECT * FROM `sjcj_test_tool` WHERE sjcj_agent_bind_case_id = #{sjcjAgentBindCaseId} AND test_tool_id = #{toolId}")
	public SjcjTestTool getTestTooolConfig(String sjcjAgentBindCaseId, String toolId);

	// 根据toolId获取其测试工具名称、测试工具标识及其默认路径
	@Select("SELECT tools_name, tools_code, tools_location FROM `running_tools_list` WHERE id = #{toolId}")
	Map<String, String> getToolInfo(String toolId);
}
