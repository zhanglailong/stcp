package org.jeecg.modules.agentbindcase.mapper;

import java.util.List;

import org.jeecg.modules.agentbindcase.entity.SjcjAgentBindCase;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 客户端绑定测试用例等信息
 * @Author: jeecg-boot
 * @Date: 2021-08-05
 * @Version: V1.0
 */
public interface SjcjAgentBindCaseMapper extends BaseMapper<SjcjAgentBindCase> {

	// 根据测试用例ID获取客户端等信息
	public List<SjcjAgentBindCase> getAgentInfoByCaseId(String caseId);
}
