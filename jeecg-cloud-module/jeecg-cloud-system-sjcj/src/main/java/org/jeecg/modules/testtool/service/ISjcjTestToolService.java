package org.jeecg.modules.testtool.service;

import java.util.Map;

import org.jeecg.modules.testtool.entity.SjcjTestTool;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Description: 测试工具采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-10
 * @Version: V1.0
 */
public interface ISjcjTestToolService extends IService<SjcjTestTool> {
	// 根据sjcjAgentBindCaseId及测试工具ID获取测试工具采集配置
	SjcjTestTool getTestTooolConfig(String sjcjAgentBindCaseId, String toolId);

	// 根据toolId获取其测试工具名称、测试工具标识及其默认路径
	Map<String, String> getToolInfo(String toolId);
}
