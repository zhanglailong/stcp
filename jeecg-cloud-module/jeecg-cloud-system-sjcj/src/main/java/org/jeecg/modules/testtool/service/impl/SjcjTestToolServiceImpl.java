package org.jeecg.modules.testtool.service.impl;

import java.util.Map;

import org.jeecg.modules.testtool.entity.SjcjTestTool;
import org.jeecg.modules.testtool.mapper.SjcjTestToolMapper;
import org.jeecg.modules.testtool.service.ISjcjTestToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 测试工具采集配置表
 * @Author: jeecg-boot
 * @Date: 2021-08-10
 * @Version: V1.0
 */
@Service
public class SjcjTestToolServiceImpl extends ServiceImpl<SjcjTestToolMapper, SjcjTestTool>
		implements ISjcjTestToolService {
	@Autowired
	SjcjTestToolMapper sjcjTestToolMapper;

	@Override
	public SjcjTestTool getTestTooolConfig(String sjcjAgentBindCaseId, String toolId) {
		return sjcjTestToolMapper.getTestTooolConfig(sjcjAgentBindCaseId, toolId);
	}
	
	@Override
	public Map<String, String> getToolInfo(String toolId) {
		return sjcjTestToolMapper.getToolInfo(toolId);
	}

}
