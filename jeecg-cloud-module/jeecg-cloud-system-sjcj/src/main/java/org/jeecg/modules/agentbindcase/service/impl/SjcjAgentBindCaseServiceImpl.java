package org.jeecg.modules.agentbindcase.service.impl;

import java.util.List;

import org.jeecg.modules.agent.entity.ServerStatusEnum;
import org.jeecg.modules.agentbindcase.entity.SjcjAgentBindCase;
import org.jeecg.modules.agentbindcase.mapper.SjcjAgentBindCaseMapper;
import org.jeecg.modules.agentbindcase.service.ISjcjAgentBindCaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 客户端绑定测试用例等信息
 * @Author: jeecg-boot
 * @Date: 2021-08-05
 * @Version: V1.0
 */
@Service
public class SjcjAgentBindCaseServiceImpl extends ServiceImpl<SjcjAgentBindCaseMapper, SjcjAgentBindCase>
		implements ISjcjAgentBindCaseService {

	@Autowired
	SjcjAgentBindCaseMapper sjcjAgentBindCaseMapper;

	@Override
	public List<SjcjAgentBindCase> getAgentInfoByCaseId(String caseId) {
		List<SjcjAgentBindCase> list = sjcjAgentBindCaseMapper.getAgentInfoByCaseId(caseId);
		if (list.size() > 0) {
			for (SjcjAgentBindCase temp : list) {
				temp.setAgentStatus(ServerStatusEnum.toEnum(temp.getAgentStatus()).getDesc());
			}
		}
		return list;
	}

}
